package com.rays.pro4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.bean.CourseBean;
import com.rays.pro4.bean.SubjectBean;
import com.rays.pro4.bean.TimeTableBean;
import com.rays.pro4.exception.ApplicationException;
import com.rays.pro4.exception.DatabaseException;
import com.rays.pro4.exception.DuplicateRecordException;
import com.rays.pro4.util.JDBCDataSource;

/**
 * The Class TimeTableModel.
 * 
 * @author Pankaj Patidar
 *
 */
public class TimeTableModel {

	private static Logger log = Logger.getLogger(TimeTableModel.class);
     
	/**
	 * To create non business primary key
	 *
	 */
	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_TIMETABLE");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	/**
	 * Add Record in database
	 * 
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(TimeTableBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		Connection conn = null;
		int pk = 0;

		CourseModel cModel = new CourseModel();
		CourseBean CourseBean = cModel.FindByPK(bean.getCourseId());
		bean.setCourseName(CourseBean.getName());

		SubjectModel smodel = new SubjectModel();
		SubjectBean SubjectBean = smodel.FindByPK(bean.getSubjectId());
		bean.setSubjectName(SubjectBean.getSubjectName());
//		System.out.println("______________________________>>>>>"+bean.getExamDate());
		// TimeTableModel model = new TimeTableModel();

//		TimeTableBean bean1 = checkBycds(bean.getCourseId(), bean.getSemester(),  new java.sql.Date(bean.getExamDate().getTime()));
//		TimeTableBean bean2 = checkBycss(bean.getCourseId(), bean.getSubjectId(), bean.getSemester());
//		 if(bean1 != null || bean2 != null){ 
//			 throw new DuplicateRecordException("TimeTable Already Exsist"); 
//			 
//		 }
//		

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT st_timetable values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setLong(2, bean.getCourseId());
			pstmt.setString(3, bean.getCourseName());
			pstmt.setLong(4, bean.getSubjectId());
			pstmt.setString(5, bean.getSubjectName());
			pstmt.setString(6, bean.getSemester());
			pstmt.setDate(7, new java.sql.Date(bean.getExamDate().getTime()));
			System.out.println("Date______________________________________________" + bean.getExamDate());
			pstmt.setString(8, bean.getExamTime());
			pstmt.setString(9, bean.getDescription());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());
			int i = pstmt.executeUpdate();
			System.out.println("record inserted" + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception....", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				// throw new ApplicationException("Exception : add rollback Exception" +
				// ex.getMessage());
			}
//			throw new ApplicationException("Exception : Exception in add timetable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;

	}

	/**
	 * Delete Record from database
	 * 
	 * @param bean
	 * @throws ApplicationException
	 */
	public void delete(TimeTableBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from ST_timetable where ID=?");
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("record delete " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : delete Rollback Exception" + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Timeteble");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete End");
	}

	/**
	 * Update Record in Database
	 * 
	 * @param tb
	 * @throws ApplicationException
	 */
	public void update(TimeTableBean tb) throws ApplicationException {
		// log.debug("TIMETABLE Model update method Started");
		Connection conn = null;
		CourseModel coumodel = new CourseModel();
		CourseBean coubean = coumodel.FindByPK(tb.getCourseId());
		String CourseName = coubean.getName();
		
		SubjectModel smodel = new SubjectModel();
		SubjectBean sbean = smodel.FindByPK(tb.getSubjectId());
		String subjectName = sbean.getSubjectName();
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("UPDATE ST_TIMETABLE SET Course_Id=?,Course_Name=?,SUBJECT_NAME=?,SUBJECT_ID=?,EXAM_DATE=?,EXAM_TIME=?,SEMESTER=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
			ps.setInt(1, (int) tb.getCourseId());
			ps.setString(2,CourseName);
			ps.setString(3, subjectName);
			ps.setInt(4, (int) tb.getSubjectId());
			ps.setDate(5, new java.sql.Date(tb.getExamDate().getTime()));
			ps.setString(6, tb.getExamTime());
			ps.setString(7, tb.getSemester());
			ps.setString(8, tb.getCreatedBy());
			ps.setString(9, tb.getModifiedBy());
			ps.setTimestamp(10, tb.getCreatedDatetime());
			ps.setTimestamp(11, tb.getModifiedDatetime());
			ps.setLong(12, tb.getId());
			ps.executeUpdate();
			System.out.println("--------------------");
			conn.commit();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			// log.error("database Exception....", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in Rollback of Update Method of TimeTable Model" + ex.getMessage());
			}
			throw new ApplicationException("Exception in update Method of TimeTable Model");
		}finally {
			JDBCDataSource.closeConnection(conn);
		}
	}
	
	/**
	 * Find By Pk Record in database
	 * 
	 * @param pk
	 * @return
	 * @throws ApplicationException
	 */
	public TimeTableBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBypk started");
		StringBuffer sql = new StringBuffer("select * from ST_timetable where id=?");
		TimeTableBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception .....", e);
			throw new ApplicationException("Exception : Exception in getting by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findBypk End");
		return bean;
	}

	/**
	 * Get record from database
	 * 
	 * @return
	 * @throws Exception
	 */
	public List list() throws Exception {
		return list(0, 0);
	}

	/**
	 * Get record from database
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List list(int pageNo, int pageSize) throws Exception {
		log.debug("model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from ST_timetable");

		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;
		System.out.println("111111");
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			System.out.println("444444444");
			while (rs.next()) {
				System.out.println("aghjgjhgjg");
				TimeTableBean bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
				list.add(bean);
				System.out.println("list");

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			// throw new ApplicationException("Exception : Exception in getting list");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

	/**
	 * search record from database
	 * 
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 */
	public List search(TimeTableBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * search record from database
	 * 
	 * @param bean
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws ApplicationException
	 */
	public List search(TimeTableBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search started");
		StringBuffer sql = new StringBuffer("select * from st_timetable where 1=1");
		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id=" + bean.getId());
			}
			System.out.println(bean.getCourseId());
			if (bean.getCourseId() > 0) {
				sql.append(" AND COURSE_ID=" + bean.getCourseId());
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND COURSE_NAME like '" + bean.getCourseName() + "%'");
			}
			System.out.println(bean.getSubjectId());
			if (bean.getSubjectId() > 0) {
				sql.append(" AND SUBJECT_ID=" + bean.getSubjectId());
			}
			if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
				sql.append(" AND SUBJECT_NAME like '" + bean.getSubjectName() + "%'");
			}
			if (bean.getExamDate() != null && bean.getExamDate().getTime() > 0) {
				Date d = new Date(bean.getExamDate().getTime());
				sql.append(" AND Exam_Date = '" + d + "'");
			}
		}
		if (pageNo > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		ArrayList list = new ArrayList();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getLong(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception.....", e);
			// throw new ApplicationException("Exception in getting search");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model search End");
		return list;
	}

	/**
	 * search record from database
	 * 
	 * @param CourseId
	 * @param SubjectId
	 * @param semester
	 * @return
	 * @throws ApplicationException
	 */
	public TimeTableBean checkBycss(long CourseId, long SubjectId, String semester) throws ApplicationException {
		Connection conn = null;
		TimeTableBean bean = null;
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE CourseName=? AND Subject_ID=? AND Semester=?");

		try {
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setLong(2, SubjectId);
			ps.setString(3, semester);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getInt(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Timetable Model list method End");
		return bean;
	}

	/**
	 * search record from database
	 * 
	 * @param CourseId
	 * @param Semester
	 * @param ExamDate
	 * @return
	 * @throws ApplicationException
	 */
	public TimeTableBean checkBycds(long CourseId, String Semester, Date ExamDate) throws ApplicationException {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE CourseName=? AND semester=? AND Exam_Date=?");

		Connection conn = null;
		TimeTableBean bean = null;
		Date ExDate = new Date(ExamDate.getTime());

		try {
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setString(2, Semester);
			ps.setDate(3, (java.sql.Date) ExamDate);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getInt(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("database Exception....", e);
			throw new ApplicationException("Exception in list Method of timetable Model");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Timetable Model list method End");
		return bean;

	}

	/**
	 * search record from database
	 * 
	 * @param CourseId
	 * @param SubjectId
	 * @param semester
	 * @param ExamDAte
	 * @return
	 */
	public static TimeTableBean checkBysemester(long CourseId, long SubjectId, String semester,
			java.util.Date ExamDAte) {

		TimeTableBean bean = null;

		Date ExDate = new Date(ExamDAte.getTime());

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM TIMETABLE WHERE CourseName=? AND SUBJECT_ID=? AND" + " SEMESTER=?");

		try {
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			ps.setLong(2, SubjectId);
			ps.setString(3, semester);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getInt(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * search record from database
	 * 
	 * @param CourseId
	 * @param ExamDate
	 * @return
	 */
	public static TimeTableBean checkByCourseName(long CourseId, java.util.Date ExamDate) {
		Connection conn = null;
		TimeTableBean bean = null;

		Date Exdate = new Date(ExamDate.getTime());

		StringBuffer sql = new StringBuffer("SELECT * FROM TIMETABLE WHERE CourseName=? " + "AND EXAM_DATE=?");

		try {
			Connection con = JDBCDataSource.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ps.setLong(1, CourseId);
			// ps.setDate(2, examdate);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bean = new TimeTableBean();
				bean.setId(rs.getLong(1));
				bean.setCourseId(rs.getLong(2));
				bean.setCourseName(rs.getString(3));
				bean.setSubjectId(rs.getInt(4));
				bean.setSubjectName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setDescription(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
