package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userProjectPermissions")
public class UserProjectPermissions implements Serializable
{

	@Id
	@GeneratedValue
	@Column(name = "userprojectpermId")
	private long id;

	@Column(nullable = false)
	private String projectName;

	@Column(nullable = false)
	private String deleted;

	@Column(nullable = false)
	private String read;

	@Column(nullable = false)
	private String write;

	@Column(nullable = false)
	private String delete;

	public UserProjectPermissions()
	{
		setDeleted("N");
	}

	public UserProjectPermissions(String name, String pass)
	{
		setProjectName(name);
		setDeleted("N");
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public final void setProjectName(String name)
	{
		this.projectName = name;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProjectPermissions other = (UserProjectPermissions) obj;
		if (!projectName.equalsIgnoreCase(other.projectName))
			return false;
		return true;
	}

	public void setRead(String read)
	{
		this.read = read;
	}

	public String getRead()
	{
		return read;
	}

	public void setWrite(String write)
	{
		this.write = write;
	}

	public String getWrite()
	{
		return write;
	}

	public void setDelete(String delete)
	{
		this.delete = delete;
	}

	public String getDelete()
	{
		return delete;
	}

}
