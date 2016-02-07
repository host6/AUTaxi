package autaxi.servlets;

import javax.persistence.*;

/**
 * Created by admin on 03.12.2015.
 */
@javax.persistence.Entity
public class NewEntity {
	@GeneratedValue
	@Id
	private Long id;

	public NewEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NewEntity(String field1, String field2) {
		this.field1 = field1;
		this.field2 = field2;
	}

	@Column(length = 100)
	private String field1;

	@Column
	private String field2;

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField1() {
		return field2;
	}

	public void setField1(String field1) {
		this.field2 = field1;
	}
}
