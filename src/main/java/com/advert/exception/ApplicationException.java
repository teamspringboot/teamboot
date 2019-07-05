package com.advert.exception;

public class ApplicationException  extends BaseRuntimeException {
	
	public ApplicationException() {
		super();
	}

    public ApplicationException(String code) {
        super(code);     
        this.setCode(code);
    }
    
    public ApplicationException(String message, Exception e) {
		super(message, e);
	}
    
    public ApplicationException(String code, String param1) {
        super(code, new String[] {param1});        
    }
    
    public ApplicationException(String code, String param1, String param2) {
        super(code, new String[] {param1,param2});        
    }
    
    public ApplicationException(String code, String param1, String param2, String param3) {
        super(code, new String[] {param1,param2,param3});        
    }
    
    public ApplicationException(String code, String param1, String param2, String param3, String param4) {
        super(code, new String[] {param1,param2,param3,param4});        
    }
    /**
     * @param code
     * @param params
     */
    public ApplicationException(String code, String[] params) {
        super(code, params);        
    }
    
    public ApplicationException(String code, String[] params, Exception e) {
    	super(code, e);  
    	StringBuffer causeMessages = null;
        if (e == null || e.getMessage() == null) {            
            causeMessages = new StringBuffer();
        }
        else {
	        causeMessages = new StringBuffer(e.getMessage());
        }
        
        if(e!=null){
	        Throwable t = e.getCause();
	        while (t != null) {
	            causeMessages.append("->\t");
	            causeMessages.append(t.getMessage()); 
	            causeMessages.append("\n");
	            t = t.getCause();
	        }
        }
        
        String[] params2 = new String[params.length+1];
        for (int i=0; i<params.length; i++) {
            params2[i] = params[i];
        }
        params2[params.length] = causeMessages.toString();
        
        this.setCode(code);
        this.setParams(params2);
    }

    
}
