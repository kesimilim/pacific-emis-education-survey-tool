package org.pacific_emis.surveys.accreditation_core.database;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase;

import java.io.IOException;

import static org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase.MIGRATION_1_2;

@RunWith(AndroidJUnit4.class)
@LargeTest
class Migration_1_2_Test {
    private static final String TEST_DB = "accreditation.database";

    @Rule
    public MigrationTestHelper helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            AccreditationDatabase.class.getCanonicalName(),
            new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrateAll() throws IOException {
        // Create earliest version of the database.
        SupportSQLiteDatabase db = helper.createDatabase(TEST_DB, 1);
        db.close();

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, false, MIGRATION_1_2);
//        AccreditationDatabase appDb = Room.databaseBuilder(
//                InstrumentationRegistry.getInstrumentation().getTargetContext(),
//                AccreditationDatabase.class,
//                TEST_DB)
//                .addMigrations(MIGRATION_1_2).build();
//        appDb.getOpenHelper().getWritableDatabase();
//        appDb.close();
    }
}
