package servlets;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by Admin on 11.11.2015.
 */
@javax.persistence.Entity
public class Entity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 32)
	private String f1;
	@Column
	private Integer f2;
	@Column
	private Boolean f3;
	@Column
	private List<String> list;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Entity(String f1, Integer f2, Boolean f3, List list) {
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.list = list;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getF1() {
		return f1;
	}

	public void setF1(String f1) {
		this.f1 = f1;
	}

	public Integer getF2() {
		return f2;
	}

	public void setF2(Integer f2) {
		this.f2 = f2;
	}

	public Boolean getF3() {
		return f3;
	}

	public void setF3(Boolean f3) {
		this.f3 = f3;
	}
}
