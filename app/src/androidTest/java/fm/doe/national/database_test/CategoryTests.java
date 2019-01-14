package fm.doe.national.database_test;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import fm.doe.national.data.persistence.AppDatabase;
import fm.doe.national.data.persistence.dao.CategoryDao;
import fm.doe.national.data.persistence.dao.SurveyDao;
import fm.doe.national.data.persistence.entity.Category;
import fm.doe.national.database_test.util.RoomTestData;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CategoryTests {

    private CategoryDao categoryDao;
    private AppDatabase database;

    private long testSurveyId = -1;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        categoryDao = database.getCategoryDao();

        fillSurveyTable();
    }

    @After
    public void closeDb() {
        database.close();
    }

    private void fillSurveyTable() {
        SurveyDao surveyDao = database.getSurveyDao();
        surveyDao.insert(RoomTestData.getSurveyFor_putSurveyEntityTest());
        testSurveyId = surveyDao.getAll().get(0).uid;
    }

    @Test
    public void createDeleteTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        Category testCategory = RoomTestData.getCategoryFor_createDeleteTest(testSurveyId);

        categoryDao.insert(testCategory);
        assertEquals(1, categoryDao.getAllForSurveyWithId(testSurveyId).size());

        testCategory.title = "Category Two";
        categoryDao.insert(testCategory);

        List<Category> categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(2, categoriesInDb.size());
        assertEquals("Category Two", categoriesInDb.get(1).title);

        categoryDao.delete(categoriesInDb.get(0));

        categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(1, categoriesInDb.size());
        assertEquals("Category Two", categoriesInDb.get(0).title);

        categoryDao.insert(testCategory);
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoriesInDb = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(0, categoriesInDb.size());
    }

    @Test
    public void updateTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        Category insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        assertEquals("Category One", insertedCategory.title);

        insertedCategory.title = "Category Two";
        categoryDao.update(insertedCategory);

        insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        assertEquals("Category Two", insertedCategory.title);
    }

    @Test
    public void getByIdTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));

        Category insertedCategory = categoryDao.getAllForSurveyWithId(testSurveyId).get(0);
        Category categoryById = categoryDao.getById(insertedCategory.uid);

        assertEquals(insertedCategory.title, categoryById.title);
        assertNull(categoryDao.getById(123984));
    }

    @Test
    public void cascadeDeleteTest() {
        categoryDao.deleteAllForSurveyWithId(testSurveyId);

        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));
        categoryDao.insert(RoomTestData.getCategoryFor_createDeleteTest(testSurveyId));

        List<Category> insertedCategories = categoryDao.getAllForSurveyWithId(testSurveyId);
        assertEquals(6, insertedCategories.size());

        database.getSurveyDao().deleteById(testSurveyId);

        assertEquals(0, categoryDao.getAllForSurveyWithId(testSurveyId).size());

        insertedCategories.forEach(category -> assertNull(categoryDao.getById(category.uid)));

        fillSurveyTable();
    }
}