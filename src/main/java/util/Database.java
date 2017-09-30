package util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.fasterxml.classmate.AnnotationConfiguration;

public class Database {
	private static final StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	static {
		registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble
			// building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
		}
	}

	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T> List<T> Get(Class<T> fieldType, String fieldName, Class parenType) {
		Session session = sessionFactory.openSession();
		List<T> result = (List<T>) session.createQuery("Select " + fieldName + " FROM " + getTableName(parenType)).list();
		session.close();
		return result;
	}
	
	public static <T> T Get(Class<T> t, int primaryKey) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		T result = (T) session.get(t, primaryKey);
		session.getTransaction().commit();
		session.close();
		return result;
	}
	
	public static <T> List<T> Get(Class<T> t) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<T> list = (List<T>) session.createQuery("from " + getTableName(t)).list();
		session.getTransaction().commit();
		session.close();
		return list;
	}
	
	private static String getTableName(Class t) {
		String name =  t.getSimpleName();
		int i = name.lastIndexOf(".");
		return i > 0 ? name.substring(i): name;
	}
	
	public static <T> T Get(Class<T> t, String where) {
		Session session = sessionFactory.openSession();
		T result = (T) session.createQuery("FROM " + getTableName(t) +  " where " + where).getSingleResult();
		session.close();
		return result;
	}
	
	public static <T> Boolean Exists(Class<?> t, String where) {
		Session session = sessionFactory.openSession();
		Long count = (Long) session.createQuery("SELECT count(*) FROM " + getTableName(t) + " where " + where)
				.uniqueResult();
		session.close();
		return count == 1;
	}


	public static void Delete(Object entity) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
		session.close();
	}

	public static <T> void Delete(Serializable primaryKey, Class<T> t) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		T result = session.get(t, primaryKey);
		session.delete(result);
		session.getTransaction().commit();
		session.close();
	}

	public static <key> key Save(Class<?> key, Object obj) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Object res = session.save(obj);
		session.getTransaction().commit();
		session.close();
		return (key) res;
	}

	public static void Update(Object obj) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(obj);
		session.getTransaction().commit();
		session.close();
	}

	public static void SaveOrUpdate(Object obj) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(obj);
		session.getTransaction().commit();
		session.close();
	}
}
