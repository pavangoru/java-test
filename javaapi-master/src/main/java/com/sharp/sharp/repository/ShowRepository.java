package com.sharp.sharp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sharp.sharp.entity.ContestVivo;
import com.sharp.sharp.entity.ShowDetails;

@Repository
public interface ShowRepository extends JpaRepository<ShowDetails, Integer> {
	@Query(value = "SELECT c.categoryid ,c.categoryname ,c.categorydesc ,l.languageid ,l.languagename ,l.description ,ch.channelid ,ch.channelname ,ch.channeldesc FROM showdetails s JOIN category c ON s.categoryid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid WHERE showid = ?1 ", nativeQuery = true)
	Object getShowdetailsById(int showid);

	@Query(value = "SELECT c.categoryid ,c.categoryname ,c.categorydesc ,l.languageid ,l.languagename ,l.description ,ch.channelid ,ch.channelname ,ch.channeldesc FROM showdetails s JOIN category c ON s.categorieid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid ", nativeQuery = true)
	List<Object[]> getallshows();

	/*
	 * @Query(value =
	 * "SELECT c.categoryid ,c.categoryname ,c.categorydesc ,l.languageid ,l.languagename ,l.description ,ch.channelid ,ch.channelname ,ch.channeldesc FROM showdetails s JOIN category c ON s.categorieid = c.categoryid JOIN language l ON s.languageid = l.languageid JOIN channel ch ON s.channelid = ch.channelid where userID = ?1"
	 * , nativeQuery = true) List<Object[]> getAllShowByUserId(String userId);
	 */

}
