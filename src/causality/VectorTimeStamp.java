package causality;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VectorTimeStamp implements Serializable{
	
	private Map<String,Integer> vector;
	
	public VectorTimeStamp() {
		vector = new HashMap<String,Integer>();			
	}
	
	public Map<String, Integer> getVector() {
		return vector;
	}

	public void setVector(Map<String, Integer> vector) {
		this.vector = vector;
	}

	public void inc(String user) {
		if (vector.containsKey(user)) {
			Integer value = vector.get(user);
			value++;
			vector.put(user, value);
		}		
	}
	
	public boolean isSuccessorOf(VectorTimeStamp otherVectorTimeStamp) {
		Map<String,Integer> otherVector = otherVectorTimeStamp.vector;
		String user = null;
		boolean result = true;
		
		for (Map.Entry<String, Integer> entry: vector.entrySet()) {
			 String key = entry.getKey();
			 Integer value = entry.getValue();
			
			 if(value == otherVector.get(key)+1) {
				 user = key;
			 }
		}
		
		if(user == null) {
			return false;
		}
		
		for(Map.Entry<String, Integer> entry: vector.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			
			Integer otherValue = otherVector.get(key);
			if (!key.equals(user)) {
				if (!(value.equals(otherValue))) {
					result = false;
				}
			}
		}				
		return result;
	}
		
	public boolean isEqualTo(VectorTimeStamp otherVectorTimeStamp) {
		Map<String,Integer> otherVector = otherVectorTimeStamp.vector;
		boolean result = true;
		
		for(Map.Entry<String, Integer> entry: vector.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			int otherValue = otherVector.get(key);
			
			if(!(value == otherValue)) {
				result = false;
			}
		}		
		return result;
	}
	
	public boolean isLessThan(VectorTimeStamp otherVectorTimeStamp) {
		Map<String,Integer> otherVector = otherVectorTimeStamp.vector;
		boolean result1 = true;
		boolean result2 = false;
		
		for(Map.Entry<String, Integer> entry: vector.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			int otherValue = otherVector.get(key);
			
			if(!(value <= otherValue)) {
				result1 = false;
			}
			if( value < otherValue) {
				result2 = true;
			}
		}
		
		return (result1 && result2);
	}
	
	public boolean isConcurrent(VectorTimeStamp otherVectorTimeStamp) {
		Map<String,Integer> otherVector = otherVectorTimeStamp.vector;
		boolean result1 = false;
		boolean result2 = false;
		
		for(Map.Entry<String, Integer> entry: vector.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			int otherValue = otherVector.get(key);
			
			if(otherValue > value) {
				result1 = true;
			}
			if(otherValue < value) {
				result2 = true;
			}
		}
		
		return (result1 && result2);	
	}
	
	public VectorTimeStamp deepCopy() {
		VectorTimeStamp v = new VectorTimeStamp();
		
		v.vector.putAll(this.vector);
		return v;
	}
	
	public void reset() {
		for (String key : vector.keySet()) {			
			vector.put(key, 0);
		}		
	}
	
	public static void main(String[] args) {
		VectorTimeStamp v1 = new VectorTimeStamp();
		v1.vector.put("Alice", 0);
		v1.vector.put("Bob", 0);
		
		VectorTimeStamp v2 = new VectorTimeStamp();
		v2.vector.put("Alice", 0);
		v2.vector.put("Bob", 1);
		
		v2.reset();
		
		if(v2.isSuccessorOf(v1)) {
			System.out.println("Successor");
		}
		
	}
}