package com.zack.enderplan.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zack.enderplan.App;
import com.zack.enderplan.model.bean.Plan;
import com.zack.enderplan.model.bean.Type;
import com.zack.enderplan.model.bean.TypeMark;
import com.zack.enderplan.model.bean.TypeMarkColor;
import com.zack.enderplan.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseManager {

    public static final String DB_NAME = "com.zack.enderplan.db";
    public static final String DB_TYPE_MARK = "type_mark.db";

    public static final int DB_VERSION = 1;

    public static final String DB_STR_PLAN = "plan";
    public static final String DB_STR_TYPE = "type";
    public static final String DB_STR_TYPE_CODE = "type_code";
    public static final String DB_STR_TYPE_NAME = "type_name";
    public static final String DB_STR_TYPE_MARK_COLOR = "type_mark_color";
    public static final String DB_STR_TYPE_MARK_PATTERN = "type_mark_pattern";
    public static final String DB_STR_TYPE_SEQUENCE = "type_sequence";
    public static final String DB_STR_PLAN_CODE = "plan_code";
    public static final String DB_STR_CONTENT = "content";
    public static final String DB_STR_CREATION_TIME = "creation_time";
    public static final String DB_STR_DEADLINE = "deadline";
    public static final String DB_STR_COMPLETION_TIME = "completion_time";
    public static final String DB_STR_STAR_STATUS = "star_status";
    public static final String DB_STR_REMINDER_TIME = "reminder_time";

    private SQLiteDatabase database;

    private static DatabaseManager ourInstance = new DatabaseManager();

    private DatabaseManager() {
        Context context = App.getGlobalContext();
        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(context, DB_NAME, null, DB_VERSION);
        database = dbHelper.getWritableDatabase();
    }

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    //*****************Type********************

    public void saveType(Type type) {
        if (type != null) {
            ContentValues values = new ContentValues();
            values.put(DB_STR_TYPE_CODE, type.getTypeCode());
            values.put(DB_STR_TYPE_NAME, type.getTypeName());
            values.put(DB_STR_TYPE_MARK_COLOR, type.getTypeMarkColor());
            values.put(DB_STR_TYPE_MARK_PATTERN, type.getTypeMarkPattern());
            values.put(DB_STR_TYPE_SEQUENCE, type.getTypeSequence());
            database.insert(DB_STR_TYPE, null, values);
        }
    }

    public List<Type> loadType() {
        List<Type> typeList = new ArrayList<>();
        Cursor cursor = database.query(DB_STR_TYPE, null, null, null, null, null, DB_STR_TYPE_SEQUENCE);
        if (cursor.moveToFirst()) {
            do {
                typeList.add(new Type(
                        cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_CODE)),
                        cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_NAME)),
                        cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_MARK_COLOR)),
                        cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_MARK_PATTERN)),
                        cursor.getInt(cursor.getColumnIndex(DB_STR_TYPE_SEQUENCE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return typeList;
    }

    public TypeMark queryTypeMarkByTypeCode(String typeCode) {
        TypeMark typeMark = null;
        Cursor cursor = database.query(DB_STR_TYPE, new String[]{DB_STR_TYPE_MARK_COLOR, DB_STR_TYPE_MARK_PATTERN},
                DB_STR_TYPE_CODE + " = ?", new String[]{typeCode}, null, null, null);
        if (cursor.moveToFirst()) {
            typeMark = new TypeMark(
                    cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_MARK_COLOR)),
                    cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_MARK_PATTERN))
            );
        }
        cursor.close();
        return typeMark;
    }

    public void updateType(String typeCode, ContentValues values) {
        database.update(DB_STR_TYPE, values, DB_STR_TYPE_CODE + " = ?", new String[]{typeCode});
    }

    public void updateTypeBase(String typeCode, String typeName, String typeMarkColor, String typeMarkPattern) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_NAME, typeName);
        values.put(DB_STR_TYPE_MARK_COLOR, typeMarkColor);
        values.put(DB_STR_TYPE_MARK_PATTERN, typeMarkPattern);
        updateType(typeCode, values);
    }

    public void updateTypeName(String typeCode, String typeName) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_NAME, typeName);
        updateType(typeCode, values);
    }

    public void updateTypeMark(String typeCode, String typeMarkColor, String typeMarkPattern) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_MARK_COLOR, typeMarkColor);
        values.put(DB_STR_TYPE_MARK_PATTERN, typeMarkPattern);
        updateType(typeCode, values);
    }

    public void updateTypeMarkColor(String typeCode, String typeMarkColor) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_MARK_COLOR, typeMarkColor);
        updateType(typeCode, values);
    }

    public void updateTypeMarkPattern(String typeCode, String typeMarkPattern) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_MARK_PATTERN, typeMarkPattern);
        updateType(typeCode, values);
    }

    public void updateTypeSequence(String typeCode, int typeSequence) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_SEQUENCE, typeSequence);
        updateType(typeCode, values);
    }

    public void deleteType(String typeCode) {
        database.delete(DB_STR_TYPE, DB_STR_TYPE_CODE + " = ?", new String[]{typeCode});
    }

    //*****************Plan********************

    public void savePlan(Plan plan) {
        if (plan != null) {
            ContentValues values = new ContentValues();
            values.put(DB_STR_PLAN_CODE, plan.getPlanCode());
            values.put(DB_STR_CONTENT, plan.getContent());
            values.put(DB_STR_TYPE_CODE, plan.getTypeCode());
            values.put(DB_STR_CREATION_TIME, plan.getCreationTime());
            values.put(DB_STR_DEADLINE, plan.getDeadline());
            values.put(DB_STR_COMPLETION_TIME, plan.getCompletionTime());
            values.put(DB_STR_STAR_STATUS, plan.getStarStatus());
            values.put(DB_STR_REMINDER_TIME, plan.getReminderTime());
            database.insert(DB_STR_PLAN, null, values);
        }
    }

    public List<Plan> loadPlan() {
        String planCode, content, typeCode;
        long creationTime, deadline, completionTime, reminderTime;
        int starStatus;
        List<Plan> planList = new ArrayList<>();
        String orderBy = DB_STR_CREATION_TIME + " desc, " + DB_STR_COMPLETION_TIME + " desc";
        Cursor cursor = database.query(DB_STR_PLAN, null, null, null, null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                planCode = cursor.getString(cursor.getColumnIndex(DB_STR_PLAN_CODE));
                content = cursor.getString(cursor.getColumnIndex(DB_STR_CONTENT));
                typeCode = cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_CODE));
                creationTime = cursor.getLong(cursor.getColumnIndex(DB_STR_CREATION_TIME));
                deadline = cursor.getLong(cursor.getColumnIndex(DB_STR_DEADLINE));
                completionTime = cursor.getLong(cursor.getColumnIndex(DB_STR_COMPLETION_TIME));
                starStatus = cursor.getInt(cursor.getColumnIndex(DB_STR_STAR_STATUS));
                reminderTime = cursor.getLong(cursor.getColumnIndex(DB_STR_REMINDER_TIME));
                planList.add(new Plan(planCode, content, typeCode, creationTime, deadline,
                        completionTime, starStatus, reminderTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return planList;
    }

    public void updatePlan(String planCode, ContentValues values) {
        database.update(DB_STR_PLAN, values, DB_STR_PLAN_CODE + " = ?", new String[]{planCode});
    }

    public void updateContent(String planCode, String content) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_CONTENT, content);
        updatePlan(planCode, values);
    }

    public void updateTypeOfPlan(String planCode, String typeCode) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_TYPE_CODE, typeCode);
        updatePlan(planCode, values);
    }

    public void updateDeadline(String planCode, long deadline) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_DEADLINE, deadline);
        updatePlan(planCode, values);
    }

    public void updateStarStatus(String planCode, int starStatus) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_STAR_STATUS, starStatus);
        updatePlan(planCode, values);
    }

    public void updateReminderTime(String planCode, long reminderTime) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_REMINDER_TIME, reminderTime);
        updatePlan(planCode, values);
    }

    public void updatePlanStatus(String planCode, long creationTime, long completionTime) {
        ContentValues values = new ContentValues();
        values.put(DB_STR_CREATION_TIME, creationTime);
        values.put(DB_STR_COMPLETION_TIME, completionTime);
        updatePlan(planCode, values);
    }

    public void deletePlan(String planCode) {
        database.delete(DB_STR_PLAN, DB_STR_PLAN_CODE + " = ?", new String[]{planCode});
    }

    public Plan queryPlan(String planCode) {
        Plan plan = null;
        Cursor cursor = database.query(DB_STR_PLAN, null, DB_STR_PLAN_CODE + " = ?", new String[]{planCode},
                null, null, null);
        if (cursor.moveToFirst()) {
            plan = new Plan(
                    planCode,
                    cursor.getString(cursor.getColumnIndex(DB_STR_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(DB_STR_TYPE_CODE)),
                    cursor.getLong(cursor.getColumnIndex(DB_STR_CREATION_TIME)),
                    cursor.getLong(cursor.getColumnIndex(DB_STR_DEADLINE)),
                    cursor.getLong(cursor.getColumnIndex(DB_STR_COMPLETION_TIME)),
                    cursor.getInt(cursor.getColumnIndex(DB_STR_STAR_STATUS)),
                    cursor.getLong(cursor.getColumnIndex(DB_STR_REMINDER_TIME))
            );
        }
        cursor.close();
        return plan;
    }

    public String queryContentByPlanCode(String planCode) {
        String content = "";
        Cursor cursor = database.query(DB_STR_PLAN, new String[]{DB_STR_CONTENT}, DB_STR_PLAN_CODE + " = ?",
                new String[]{planCode}, null, null, null);
        if (cursor.moveToFirst()) {
            content = cursor.getString(cursor.getColumnIndex(DB_STR_CONTENT));
        }
        cursor.close();
        return content;
    }

    public Map<String, Long> queryReminderTimeWithEnabledReminder() {
        String planCode;
        Long reminderTime;
        Map<String, Long> reminderTimeMap = new HashMap<>();
        Cursor cursor = database.query(DB_STR_PLAN, new String[]{DB_STR_PLAN_CODE, DB_STR_REMINDER_TIME},
                DB_STR_REMINDER_TIME + " > ?", new String[]{"0"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                planCode = cursor.getString(cursor.getColumnIndex(DB_STR_PLAN_CODE));
                reminderTime = cursor.getLong(cursor.getColumnIndex(DB_STR_REMINDER_TIME));
                reminderTimeMap.put(planCode, reminderTime);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminderTimeMap;
    }

    //*****************TypeMark*****************

    public List<TypeMarkColor> loadTypeMarkColor() {
        SQLiteDatabase typeMarkDB = SQLiteDatabase.openDatabase(App.getGlobalContext().getDatabasePath(DB_TYPE_MARK).getPath(), null, SQLiteDatabase.OPEN_READONLY);
        List<TypeMarkColor> typeMarkColorList = new ArrayList<>();
        boolean shouldUseChinese = Util.getPreferredLocale().equals(Locale.SIMPLIFIED_CHINESE);
        Cursor cursor = typeMarkDB.rawQuery("select * from color", null);
        if (cursor.moveToFirst()) {
            do {
                typeMarkColorList.add(new TypeMarkColor(
                        cursor.getString(cursor.getColumnIndex("color_hex")),
                        cursor.getString(cursor.getColumnIndex(shouldUseChinese ? "color_zh" : "color_en"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        typeMarkDB.close();
        return typeMarkColorList;
    }

    public String queryTypeMarkColorNameByTypeMarkColorHex(String typeMarkColorHex) {
        SQLiteDatabase typeMarkDB = SQLiteDatabase.openDatabase(App.getGlobalContext().getDatabasePath(DB_TYPE_MARK).getPath(), null, SQLiteDatabase.OPEN_READONLY);
        String columnName = Util.getPreferredLocale().equals(Locale.SIMPLIFIED_CHINESE) ? "color_zh" : "color_en";
        Cursor cursor = typeMarkDB.rawQuery("select " + columnName + " from color where color_hex = ?", new String[]{typeMarkColorHex});
        String typeMarkColorName = null;
        if (cursor.moveToFirst()) {
            typeMarkColorName = cursor.getString(cursor.getColumnIndex(columnName));
        }
        cursor.close();
        typeMarkDB.close();
        return typeMarkColorName;
    }
}
