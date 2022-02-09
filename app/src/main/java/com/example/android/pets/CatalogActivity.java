/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //displayDatabaseInfo();



    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        //mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        //Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
        String [] projection = {PetEntry._ID,
                PetEntry.COLUMN_NAME,
                PetEntry.COLUMN_BREED,
                PetEntry.COLUMN_GENDER,
                PetEntry.COLUMN_WEIGHT};

        /*Cursor cursor = db.query(PetEntry.TABLE_NAME, projection,
                null, null,
                null, null, null);*/
        Cursor cursor=getContentResolver().query(PetEntry.CONTENT_URI,projection,null,null,null);

        ListView displayView = (ListView) findViewById(R.id.list_view_pet);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        displayView.setEmptyView(emptyView);

        // Setup cursor adapter using cursor from last step
        PetCursorAdapter petAdapter = new PetCursorAdapter(this, cursor);
        // Attach cursor adapter to the ListView
        displayView.setAdapter(petAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    private void insertDummyPet() {
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_NAME, "Tobby");
        values.put(PetEntry.COLUMN_BREED, "Terrier");
        values.put(PetEntry.COLUMN_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_WEIGHT, 7);

        Uri newUri=getContentResolver().insert(PetEntry.CONTENT_URI,values);
        Log.v("CatalogActivity", "New Row Id"+newUri);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now

                //my code to delete dummy pet Tobby
                String selection=PetEntry.COLUMN_NAME + "=?";
                String[] selectionArgs = new String[]{"Tobby"};
                int Anzahl=getContentResolver().delete(PetEntry.CONTENT_URI, selection, selectionArgs);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}