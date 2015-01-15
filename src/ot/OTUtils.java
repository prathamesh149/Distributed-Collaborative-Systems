package ot;

public class OTUtils {
	public static int[] deepCopy(int[] arr) {
		int[] copyArr = new int[arr.length];
		
		for(int i=0; i<arr.length; i++) {
			copyArr[i] = arr[i];
		}		
		return copyArr;
	}
	
	public static int[] flip(int[] arr) {
		int[] flipArr = new int[2];
		
		flipArr[0] = arr[1];
		flipArr[1] = arr[0];
		
		return flipArr;
	}
	
	public static boolean isConcurrentTo(int[] a, int[] b) {
		if ( a[0] > b[0] && a[1] < b[1]) {
			return true;
		} else if (a[0] < b[0] && a[1] > b[1]) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	public static MessageWithOTTimeStamp deepCopy(MessageWithOTTimeStamp msg) {
		
		MessageWithOTTimeStamp copy = new AMessageWithOTTimeStamp(message, otTimeStamp, user)
		return msg;
	}
	*/
}
