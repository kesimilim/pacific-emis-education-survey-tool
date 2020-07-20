import 'package:pacific_dashboards/models/accreditations/accreditation_chunk.dart';
import 'package:pacific_dashboards/models/emis.dart';
import 'package:pacific_dashboards/models/exam/exam.dart';
import 'package:pacific_dashboards/models/lookups/lookups.dart';
import 'package:pacific_dashboards/models/pair.dart';
import 'package:pacific_dashboards/models/school/school.dart';
import 'package:pacific_dashboards/models/school_enroll/school_enroll.dart';
import 'package:pacific_dashboards/models/teacher/teacher.dart';

abstract class Database {
  LookupsDao get lookups;
  StringsDao get strings;
  SchoolsDao get schools;
  TeachersDao get teachers;
  ExamsDao get exams;
  AccreditationsDao get accreditations;
  SchoolEnrollDao get schoolEnroll;
  DistrictEnrollDao get districtEnroll;
  NationEnrollDao get nationEnroll;
}

abstract class LookupsDao {
  Future<void> save(Lookups lookups, Emis emis);
  Future<Pair<bool, Lookups>> get(Emis emis);
}

abstract class StringsDao {
  Future<void> save(String key, String string);
  Future<String> getByKey(String key, {String defaultValue});
}

abstract class SchoolsDao {
  Future<void> save(List<School> schools, Emis emis);
  Future<List<School>> get(Emis emis);
}

abstract class TeachersDao {
  Future<void> save(List<Teacher> teachers, Emis emis);
  Future<Pair<bool, List<Teacher>>> get(Emis emis);
}

abstract class ExamsDao {
  Future<void> save(List<Exam> exams, Emis emis);
  Future<Pair<bool, List<Exam>>> get(Emis emis);
}

abstract class AccreditationsDao {
  Future<void> save(AccreditationChunk chunk, Emis emis);
  Future<AccreditationChunk> get(Emis emis);
}

abstract class SchoolEnrollDao {
  Future<void> save(String schoolId, List<SchoolEnroll> enroll, Emis emis);
  Future<List<SchoolEnroll>> get(String schoolId, Emis emis);
}

abstract class DistrictEnrollDao {
  Future<void> save(String districtCode, List<SchoolEnroll> enroll, Emis emis);
  Future<List<SchoolEnroll>> get(String districtCode, Emis emis);
}

abstract class NationEnrollDao {
  Future<void> save(List<SchoolEnroll> enroll, Emis emis);
  Future<List<SchoolEnroll>> get(Emis emis);
}