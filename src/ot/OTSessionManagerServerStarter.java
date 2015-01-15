package ot;

import java.util.HashMap;
import java.util.Map;

import trace.ot.OTIMTracerSetter;
import trace.ot.OTTraceChecker;
import util.annotations.Tags;
import util.session.ASessionManager;
import util.session.ASessionManagerSelector;
import util.session.ServerMessageFilterCreator;
import util.session.ServerSentMessageFilterSelector;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.ImplicitKeywordKind;
import util.trace.MessagePrefixKind;
import util.trace.Tracer;
import util.trace.session.ServerClientJoined;

@Tags({DistributedTags.SERVER, DistributedTags.SESSION_MANAGER, ApplicationTags.IM})
public class OTSessionManagerServerStarter {

	static ASessionManager server;
	public static void main (String[] args) {
		
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
		Tracer.setMessagePrefixKind(MessagePrefixKind.FULL_CLASS_NAME);

		Tracer.setKeywordPrintStatus(ServerClientJoined.class, true);
		Tracer.showInfo(true);
		
		OTIMTracerSetter.traceOTIM();
		
		Tracer.setKeywordPrintStatus(ServerClientJoined.class, true);

		//SessionTracerSetter.setSessionPrintStatus();
		
		Map<String,ServerFilter> serverFilterMap = new HashMap<String,ServerFilter>();
		serverFilterMap.put("history", new ServerFilter());
		serverFilterMap.put("topic", new ServerFilter());
		
		ServerMessageFilterCreator sentMessageQueuerCreator = new AMasterOTTimeStampingSentMessageFilterCreator(serverFilterMap);
		
		//ServerMessageFilterCreator does not extend MessageFilterCreator<SentMessage>
		ServerSentMessageFilterSelector.setMessageFilterFactory(sentMessageQueuerCreator);
		
		server = ASessionManagerSelector.getSessionManager();
		server.register();		
		
		(new OTServerCheckBoxFrame()).createAndShowCheckBoxFrame((AMasterOTTimeStampingSentMessageFilter) sentMessageQueuerCreator.getServerMessageFilter());
		
	}
}