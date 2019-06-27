import 'dart:async';

import '../models/schools_model.dart';
import '../models/teachers_model.dart';

abstract class Provider {
  Future<TeachersModel> fetchTeachersModel();

  Future<SchoolsModel> fetchSchoolsModel();
}
