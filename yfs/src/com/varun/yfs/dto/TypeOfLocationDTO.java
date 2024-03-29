package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class TypeOfLocationDTO extends BaseModelData
{
	private static final long serialVersionUID = -6904705980428989869L;
	private long id;

	public TypeOfLocationDTO()
	{
		set("deleted", "N");
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return get("name");
	}

	public void setName(String name)
	{
		set("name", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		TypeOfLocationDTO refType = (TypeOfLocationDTO) obj;

		if (this.getName().equalsIgnoreCase(refType.getName()))
			return true;

		return false;
	}
}
