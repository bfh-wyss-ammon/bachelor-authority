package util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.fasterxml.classmate.AnnotationConfiguration;

import data.Entity;

public class DatabaseManager {
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
		List<T> list = (List<T>) session.createQuery("from " + GetTableName(t)).list();
		session.getTransaction().commit();
		session.close();
		return list;
	}
	
	public static void Delete(Entity entity) {
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
	
	public static String GetTableName(Class<?> t) {
		Annotation[] annotions = t.getAnnotations();
		if (annotions != null && annotions.length > 0) {
			for (int i = 0; i < annotions.length; i++) {
				if (annotions[i].annotationType() == Entity.class) {
					Entity entity = (Entity) annotions[i];
					return entity.dbTableName();
				}
			}
		}

		System.out.println("DatabaseManager.GetTableName: no annotions found");
		return "";
	}
}
