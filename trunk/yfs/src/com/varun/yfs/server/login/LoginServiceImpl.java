package com.varun.yfs.server.login;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.login.rpc.LoginService;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.models.data.DataUtil;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
	private static final long serialVersionUID = 4456105400553118785L;

	@Override
	public UserDTO loginServer(String name, String password)
	{
		if (name == null || password == null)
			return null;

		List<UserDTO> modelList = DataUtil.<UserDTO> getModelList("User");
		UserDTO user = new UserDTO(name, password);
		int usrIdx = modelList.indexOf(user);

		UserDTO userDbModel = modelList.get(usrIdx);

		if (usrIdx >= 0) // user-name exists
		{
			if (userDbModel.getPassword().equalsIgnoreCase(password))
			{
				user = new UserDTO(userDbModel.getProperties());
				user.setName(name);
				user.setPassword(password);
				
				user.setLoggedIn(true);
				user.setSessionId(this.getThreadLocalRequest().getSession().getId());
				
				storeUserInSession(user);
			}
		}

		return user;
	}

	@Override
	public UserDTO loginFromSessionServer()
	{
		return getUserAlreadyFromSession();
	}

	@Override
	public void logout()
	{
		deleteUserFromSession();
	}

	@Override
	public boolean changePassword(String name, String newPassword)
	{

		List<UserDTO> modelList = DataUtil.<UserDTO> getModelList("User");
		UserDTO user = new UserDTO(name, "");
		int usrIdx = modelList.indexOf(user);

		if (usrIdx >= 0) // user-name exists
		{
			UserDTO userDTO = modelList.get(usrIdx);

			userDTO.setPassword(newPassword);

			DataUtil.saveUser(userDTO);

			return true;
		}

		return false;
	}

	private UserDTO getUserAlreadyFromSession()
	{
		UserDTO user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("user");
		if (userObj != null && userObj instanceof UserDTO)
		{
			user = (UserDTO) userObj;
		}
		return user;
	}

	private void storeUserInSession(UserDTO user)
	{
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);
		session.setAttribute("user", user);
	}

	private void deleteUserFromSession()
	{
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute("user");
	}

}
