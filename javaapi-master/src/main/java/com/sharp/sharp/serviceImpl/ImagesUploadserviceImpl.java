package com.sharp.sharp.serviceImpl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.sharp.entity.Category;
import com.sharp.sharp.entity.ImagesEntity;
import com.sharp.sharp.repository.ImageUpoadRespository;
import com.sharp.sharp.service.ImageUploadService;
import com.sharp.sharp.util.Constants;
import com.sharp.sharp.util.Sharp6Validation;

@Component
@Transactional
public class ImagesUploadserviceImpl implements ImageUploadService {

	@Autowired
	private ImageUpoadRespository imageDao;
	@Autowired
	private EntityManager entityManager;

	@Override
	public ImagesEntity upoadImages(ImagesEntity images) {
		// TODO Auto-generated method stub
		ImagesEntity savedImages = new ImagesEntity();
		try {
			savedImages = imageDao.save(images);
		} catch (Exception e) {
			// TODO: handle exception
			savedImages = null;
		}
		return savedImages;
	}

	@Override
	public List<ImagesEntity> getAllImagesById() {
		// TODO Auto-generated method stub
		try {
			List<ImagesEntity> findAllById = imageDao.findAll();

			return findAllById;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	@Override
	public ImagesEntity getImage(long id) {
		// TODO Auto-generated method stub
		try {
			ImagesEntity imageObj = imageDao.getImageById(id);
			return imageObj;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public ImagesEntity updateImageName(String shortName, long Id) {
		// TODO Auto-generated method stub
		Session session = entityManager.unwrap(Session.class);
		try {
			Query query = session.createQuery("update file_name=" + shortName + " FROM users WHERE id = " + Id);
			List<ImagesEntity> list = query.list();
			return list.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public String deleteImageById(long id) {
		Session session = entityManager.unwrap(Session.class);

		try {

			SQLQuery query = session.createSQLQuery("DELETE from images_entity where id =" + id);
			query.executeUpdate();

			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String deleteImageById(long id, int categoryId) {
		Session session = entityManager.unwrap(Session.class);

		try {
			/*
			 * Criteria criteria = session.createCriteria(Category.class);
			 * criteria.add(Restrictions.eq("categoryid", categoryId)); List<Category> list
			 * = criteria.list();
			 */

			Query query2 = session
					.createQuery("update Category m set m.categoryImageUrl = null where m.id = " + categoryId);
			query2.executeUpdate();
			session.close();
			SQLQuery query = session.createSQLQuery("DELETE from images_entity where id =" + id);
			query.executeUpdate();
			session.close();
			return Constants.SUCCESS;

		} catch (Exception e) {
			// TODO: handle exception
			return Constants.FAILURE;
		}

	}

}
