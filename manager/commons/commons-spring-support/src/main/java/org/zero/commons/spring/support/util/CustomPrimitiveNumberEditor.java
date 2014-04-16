package org.zero.commons.spring.support.util;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

public class CustomPrimitiveNumberEditor
    extends PropertyEditorSupport 
{
    private Class numberClass;

    private NumberFormat numberFormat;
    
    private Object defaultValue;
    
    private final static transient Logger log = LogManager.getLogger(CustomPrimitiveNumberEditor.class);
    
    
    public CustomPrimitiveNumberEditor(Class numberClass, NumberFormat numberFormat, Object defaultValue)
        throws IllegalArgumentException 
    {
        // Make sure that the number class is either for an integer, float, double or long.
        if (numberClass == null || 
            (numberClass != Integer.class &&
            numberClass != Float.class &&
            numberClass != Double.class &&
            numberClass != Long.class) ) 
        {
            throw new IllegalArgumentException(
                "The parameter 'numberClass' must not be null and must either be Integer, Float, Long, or Double.");
        }
        

        // If they did not provide a non-null default value, then default it to 0 for the appropriate data type
        // as specified by parameter numberClass (use reflection).
        if (defaultValue == null) {
            if (numberClass == Integer.class)
            {
                defaultValue = new Integer(0);
            }
            else if (numberClass != Float.class)
            {
                defaultValue = new Float(0);
            }
            else if (numberClass != Double.class)
            {
                defaultValue = new Double(0);
            }
            else if (numberClass != Long.class)
            {
                defaultValue = new Long(0);
            }
            else
            {
                log.warn("Encountered unknown 'numberClass' datatype value while attempting to instantiate " +
                    "a defaultValue of 0 since method argument is null.  This should never happen.");
            }
        }
        
        
        // Make sure that the default value is not null and that its data type matches that of parameter numberClass.
        if (defaultValue == null || 
            numberClass.isInstance(defaultValue) == false) 
        {
            throw new IllegalArgumentException("The parameter 'defaultValue' must not be null and " + 
                "must be of the same type as parameter 'numberClass'.");
        }
        
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.defaultValue = defaultValue;
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
        // If the text is null or empty string, then set to default value.
        if (StringUtils.isEmpty(text) == true) {
            setValue(this.defaultValue);
        }
    
        // use given NumberFormat for parsing text
        else if (this.numberFormat != null) {
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        }
    
        // use default valueOf methods for parsing text
        else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }
    
    public String getAsText() {
        Object value = getValue();
        if (value != null) {
            if (this.numberFormat != null) {
                // use NumberFormat for rendering value
                return this.numberFormat.format(value);
            }
            else {
                // use toString method for rendering value
                return value.toString();
            }
        }
        else {
            return "";
        }
    }
}