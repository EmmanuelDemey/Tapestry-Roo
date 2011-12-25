import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;

@Import(stylesheet = "context:css/style.css")
public class Layout {

	@Parameter
	private String title;
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}
