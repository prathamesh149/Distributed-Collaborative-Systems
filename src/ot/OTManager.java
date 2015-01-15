package ot;

import im.ListEdit;
import im.TopicListEdit;

import java.util.ArrayList;
import java.util.List;

import trace.echo.ListEditInfo;
import trace.ot.ConcurrentEdits;
import trace.ot.InitialOTTimeStampCreated;
import trace.ot.LocalSiteCountIncremented;
import trace.ot.OTListEditRemoteCountIncremented;
import trace.ot.RemoteSiteCountIncremented;
import trace.ot.TransformationResult;
import trace.ot.UserOTTimeStampInfo;
import util.Misc;
import util.trace.session.MessageBuffered;
import util.trace.session.MessageUnBuffered;

public class OTManager {
	
	private int[] myTimeStamp;
	private String myName;
	private boolean isServer;
	private List<MessageWithOTTimeStamp> localBuffer;
	
	public OTManager(String userName, boolean isServer) {
		myName = userName;
		this.isServer = isServer;
		
		//local index is 0, remote is 1
		myTimeStamp = new int[2];
		
		
		initMyTimeStamp();
		InitialOTTimeStampCreated.newCase("", userName, myTimeStamp[0], myTimeStamp[1], isServer, this);
		localBuffer = new ArrayList<MessageWithOTTimeStamp>();
	}

	public int[] getMyTimeStamp() {
		return myTimeStamp;
	}

	public void setMyTimeStamp(int[] myTimeStamp) {
		this.myTimeStamp = myTimeStamp;
	}

	public String getMyName() {
		return myName;
	}

	public boolean isServer() {
		return isServer;
	}
	
	public void incLocalTimeStamp(){
		myTimeStamp[0]++;
		LocalSiteCountIncremented.newCase("", myName, myTimeStamp[0], myTimeStamp[1], this);
	}
	
	public void incRemoteTimeStamp() {
		myTimeStamp[1]++;
		RemoteSiteCountIncremented.newCase("", myName, myTimeStamp[0], myTimeStamp[1], this);
	}
	
	public void put(MessageWithOTTimeStamp msg) {
		localBuffer.add(msg);
		MessageBuffered.newCase("", msg, "", localBuffer, this);
	}
	
	public void initMyTimeStamp() {
		myTimeStamp[0] = 0;
		myTimeStamp[1] = 0;
	}

	public void resetMyTimeStamp() {
		initMyTimeStamp();
	}
	
	public void resetLocalBuffer() {
		int size = localBuffer.size();
		for(int i = 0; i < size; i++) {
			localBuffer.remove(0);
		}
	}
	
	public MessageWithOTTimeStamp transformReceived(MessageWithOTTimeStamp msg) {
		
		MessageWithOTTimeStamp transformMsg = null;
		
		for(int i=0; i<localBuffer.size();i++) {
			if (!OTUtils.isConcurrentTo(localBuffer.get(i).getOTTimeStamp(),
					msg.getOTTimeStamp())) {
				MessageUnBuffered.newCase("",localBuffer.get(i),"", localBuffer, this);
				localBuffer.remove(i);
				
				i--;
			}
		}
		
		for(int i=0;i<localBuffer.size();i++) {
			transformMsg = transform(msg,localBuffer.get(i));
			
			localBuffer.set(i, transform(localBuffer.get(i),msg));
			
			//Increment remote time stamp of ith value
			int[] temp = localBuffer.get(i).getOTTimeStamp();
			temp[1]++;
			
			msg = transformMsg;
		}
		
		incRemoteTimeStamp();
		
		return msg;
	}
	
	public MessageWithOTTimeStamp transform(MessageWithOTTimeStamp R, MessageWithOTTimeStamp L) {
		MessageWithOTTimeStamp RT= (MessageWithOTTimeStamp)Misc.deepCopy(R);
		
		
		ListEditInfo aListEdit = null;
		
		
		
		if (R.getMessage() instanceof ListEdit) {
			ListEdit r = (ListEdit)R.getMessage();
			ListEdit l = (ListEdit)L.getMessage();
			ListEdit rt = (ListEdit)RT.getMessage();
			
			if((r.getIndex() > l.getIndex()) ||
					(r.getIndex() == l.getIndex() && !R.isServer())
					) {
				rt.setIndex(r.getIndex()+1);
				//RT.setMessage(rt);
			}
			
			
		}
		
		if (R.getMessage() instanceof TopicListEdit) {
			TopicListEdit r = (TopicListEdit)R.getMessage();
			TopicListEdit l = (TopicListEdit)L.getMessage();
			TopicListEdit rt = (TopicListEdit)RT.getMessage();
			
			if((r.getIndex() > l.getIndex()) ||
					(r.getIndex() == l.getIndex() && !R.isServer())
					) {
				rt.setIndex(r.getIndex()+1);
				//RT.setMessage(rt);
			}
			
		}
		
		
		return RT;
	}
}