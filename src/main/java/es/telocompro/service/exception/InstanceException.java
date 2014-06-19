package es.telocompro.service.exception;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings("serial")
public abstract class InstanceException extends Exception {

    private Object key;
    private String className;

    protected InstanceException(String specificMessageException, Object key, String className) {
        super(specificMessageException + " - (key = '" + key + "') - (className = '" + className + "')");
        this.key = key;
        this.className = className;
    }

    public Object getKey() {
        return key;
    }

    public String getClassName() {
        return className;
    }
}
