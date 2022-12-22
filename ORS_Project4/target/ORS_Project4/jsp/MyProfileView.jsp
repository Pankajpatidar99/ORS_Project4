<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.rays.pro4.controller.MyProfileCtl"%>
<%@page import="com.rays.pro4.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.util.DataUtility"%>
<%@page import="com.rays.pro4.util.ServletUtility"%>

<!DOCTYPE html>
<html>
<head>

<title>Insert title here</title>
 <script type="text/javascript" src="../js/calendar.js"></script>
       
</head>
<body>
 <jsp:useBean id="bean" class="com.rays.pro4.bean.UserBean"
        scope="request"></jsp:useBean>
<form action="<%=ORSView.MY_PROFILE_CTL%>" method="post">
        <%@include file="Header.jsp"%>
       

        <center>
            <h1>My Profile</h1>
            
            <H2>
                <font color="green"> <%=ServletUtility.getSuccessMessage(request)%></font>
                <font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
            </H2>
            <input type="hidden" name="id" value="<%=bean.getId()%>">
            <input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
            <input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
            <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
            <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
            

            <table>
                <tr>
                    <th align="left">LoginId<span style="color: red">*</span> :</th>
                    <td><input type="text" name="login" size="26"
                        value="<%=DataUtility.getStringData(bean.getLogin())%>"readonly="readonly"></td>
                        <td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("login", request)%></font>
                        </td>
                </tr>

                <tr>
                    <th align="left">First Name<span style="color: red">*</span> :</th>
                    <td ><input type="text" name="firstName" size="26"
                        value="<%=DataUtility.getStringData(bean.getFirstName())%>"></td>
                        <td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("firstName", request)%></font>
                        </td>
                </tr>
                
                <tr>
                    <th align="left">Last Name<span style="color: red">*</span> :</th>
                    <td><input type="text" name="lastName" size="26"
                        value="<%=DataUtility.getStringData(bean.getLastName())%>"></td>
                        <td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("lastName", request)%></font>
                        </td>
                </tr>
               <tr>
					<th align="left">Gender <span style="color: red">*</span></th>
					<td>
						<%
							HashMap map = new HashMap();
							map.put("Male", "Male");
							map.put("Female", "Female");
						%><%=HTMLUtility.getList("gender", bean.getGender(), map)%>
						
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("gender", request)%></font></td>
				</tr>
                <tr>
                    <th align="left">Mobile No<span style="color: red">*</span> :</th>
                    <td><input type="text" name="mobileNo" size="26"
                        value="<%=DataUtility.getStringData(bean.getMobileNo())%>"></td>
                        <td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("mobileNo", request)%></font>
                        </td>
                </tr>

                <tr>
                    <th align="left">Date Of Birth (mm/dd/yyyy)</th>
                    <td><input type="text" name="dob" readonly="readonly" size="26"
                        value="<%=DataUtility.getDateString(bean.getDob())%>">
                 <!--   <a href="javascript:getCalendar(document.forms[0].dob);">
                            <img src="../img/cal.jpg" width="16" height="15" border="0"
                            alt="Calender">
                    </a> --><font
                        color="red"> <%=ServletUtility.getErrorMessage("dob", request)%></font></td>
                </tr>
                
            
                
                <tr>
                    <th></th>
                    <td colspan="2"><input type="submit" name="operation"
                        value="<%=MyProfileCtl.OP_CHANGE_MY_PASSWORD %>"> &nbsp; <input type="submit"
                        name="operation" value="<%=MyProfileCtl.OP_SAVE %>"> &nbsp;</td>
                </tr>
            </table>
    </form>
    </center>
    <%@ include file="Footer.jsp"%>


</body>
</html>