package org.dacapo.analysis.util.events;

import org.dacapo.analysis.util.CSVInputStream;
import org.dacapo.analysis.util.CSVOutputStream;
import org.dacapo.analysis.util.CSVInputStream.NoFieldAvailable;
import org.dacapo.analysis.util.CSVInputStream.ParseError;
import org.dacapo.instrument.LogTags;

public class EventAllocation extends Event implements EventHasThread {

	public  static final String TAG = LogTags.LOG_PREFIX_ALLOCATION;
	
	private long   objectTag;
	private long   allocClassTag;
	private String allocClass;
	private long   threadTag;
	private long   threadClassTag;
	private String threadClass;
	private String threadName;
	private long   size;
	
	public EventAllocation(long time,      long objectTag,      
						   long classTag,  String className,
						   long threadTag, long threadClassTag, String threadClassName, String threadName,
						   long size) {
		super(time);
		this.objectTag       = objectTag;
		this.allocClassTag   = classTag;
		this.allocClass      = className;
		this.threadTag       = threadTag;
		this.threadClassTag  = threadClassTag;
		this.threadClass     = threadClassName;
		this.threadName      = threadName;
		this.size            = size;
	}

	public String getLogPrefix() {
		return TAG;
	}
	
	protected void writeEvent(CSVOutputStream os) {
		os.write(""+getTime());
		os.write(""+objectTag);
		os.write(""+allocClassTag);
		os.write(allocClass);

		EventThread.write(os, this);

		os.write(""+size);
	}
	

	public long getObjectTag() { return objectTag; }
	
	public long getAllocClassTag() { return allocClassTag; }
	
	public String getAllocClass() { return allocClass; }
	
	public long getThreadTag() { return threadTag; }
	public void setThreadTag(long threadTag) { this.threadTag = threadTag; }
	
	public long getThreadClassTag() { return threadClassTag; }
	public void setThreadClassTag(long threadClassTag) { this.threadClassTag = threadClassTag; }
	
	public String getThreadClass() { return threadClass; }
	public void setThreadClass(String threadClass) { this.threadClass = threadClass;  }
	
	public String getThreadName() { return threadName; }
	public void setThreadName(String threadName) { this.threadName = threadName; }
	
	public long getSize() { return size; }
	
	EventAllocation(CSVInputStream is) throws NoFieldAvailable, ParseError, EventParseException {
		super(is);
		
		EventThread.read(is, this);
		
		this.objectTag       = is.nextFieldLong();

		this.allocClassTag   = is.nextFieldLong();
		this.allocClass      = is.nextFieldString();

		this.size            = is.nextFieldLong();
		
		if (is.numberOfFieldsLeft()!=0 && this instanceof EventAllocation)
			throw new EventParseException("additional field",null);
	}
}
