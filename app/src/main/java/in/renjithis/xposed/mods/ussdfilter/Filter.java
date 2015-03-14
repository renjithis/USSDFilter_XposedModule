package in.renjithis.xposed.mods.ussdfilter;

public class Filter {
	
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String name;
	public FilterType type;
	public String subStringRegEx;
	public int priority;
	public OutputType outputType;
	public Boolean enabled;

}
