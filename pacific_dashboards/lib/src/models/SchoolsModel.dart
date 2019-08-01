import 'dart:core';

import "package:collection/collection.dart";
import 'SchoolModel.dart';
import '../resources/Filter.dart';

class SchoolsModel {
  Map<String, Filter> _filters;
  List<SchoolModel> _schools;

  List<SchoolModel> get schools => _schools;

  Filter get yearFilter => _filters['year'];
  Filter get stateFilter => _filters['state'];
  Filter get authorityFilter => _filters['authority'];
  Filter get govtFilter => _filters['govt'];
  Filter get schoolTypeFilter => _filters['schoolType'];
  Filter get ageFilter => _filters['age'];
  Filter get schoolLevelFilter => _filters['schoolLevel'];

  SchoolsModel.fromJson(List parsedJson) {
    _schools = List<SchoolModel>();
    _schools = parsedJson.map((i) => SchoolModel.fromJson(i)).toList();
    _createFilters();
  }

  List toJson() {
    return _schools.map((i) => (i).toJson()).toList();
  }

  Map<dynamic, List<SchoolModel>> getSortedWithFiltersByState() {
    var filteredList = _schools
        .where((i) => yearFilter.isEnabledInFilter(i.surveyYear.toString()))
        .where((i) => authorityFilter.isEnabledInFilter(i.authorityCode))
        .where((i) => govtFilter.isEnabledInFilter(i.authorityGovt))
        .where((i) => schoolTypeFilter.isEnabledInFilter(i.schoolTypeCode))
        .where((i) => ageFilter.isEnabledInFilter(i.ageGroup))
        .where((i) => schoolLevelFilter.isEnabledInFilter(i.classLevel));

    return groupBy(filteredList, (obj) => obj.districtCode);
  }

  Map<dynamic, List<SchoolModel>> getSortedByState() {
    return groupBy(_schools, (obj) => obj.districtCode);
  }

  Map<dynamic, List<SchoolModel>> getSortedWithFiltersByAuthority() {
    var filteredList = _schools
        .where((i) => yearFilter.isEnabledInFilter(i.surveyYear.toString()))
        .where((i) => stateFilter.isEnabledInFilter(i.districtCode))
        .where((i) => govtFilter.isEnabledInFilter(i.authorityGovt))
        .where((i) => schoolTypeFilter.isEnabledInFilter(i.schoolTypeCode))
        .where((i) => ageFilter.isEnabledInFilter(i.ageGroup))
        .where((i) => schoolLevelFilter.isEnabledInFilter(i.classLevel));

    return groupBy(filteredList, (obj) => obj.authorityCode);
  }

  Map<dynamic, List<SchoolModel>> getSortedByAuthority() {
    return groupBy(_schools, (obj) => obj.authorityCode);
  }

  Map<dynamic, List<SchoolModel>> getSortedWithFiltersByGovt() {
    var filteredList = _schools
        .where((i) => stateFilter.isEnabledInFilter(i.districtCode))
        .where((i) => yearFilter.isEnabledInFilter(i.surveyYear.toString()))
        .where((i) => authorityFilter.isEnabledInFilter(i.authorityCode))
        .where((i) => schoolTypeFilter.isEnabledInFilter(i.schoolTypeCode))
        .where((i) => ageFilter.isEnabledInFilter(i.ageGroup))
        .where((i) => schoolLevelFilter.isEnabledInFilter(i.classLevel));

    return groupBy(filteredList, (obj) => obj.authorityGovt);
  }

  Map<dynamic, List<SchoolModel>> getSortedByGovt() {
    return groupBy(_schools, (obj) => obj.authorityGovt);
  }

  List<dynamic> getDistrictCodeKeysList() {
    var statesGroup = groupBy(_schools, (obj) => obj.districtCode);

    return statesGroup.keys.toList();
  }

  Map<dynamic, List<SchoolModel>> getSortedWithFilteringBySchoolType() {
    var filteredList = _schools
        .where((i) => stateFilter.isEnabledInFilter(i.districtCode))
        .where((i) => yearFilter.isEnabledInFilter(i.surveyYear.toString()))
        .where((i) => authorityFilter.isEnabledInFilter(i.authorityCode))
        .where((i) => ageFilter.isEnabledInFilter(i.ageGroup))
        .where((i) => govtFilter.isEnabledInFilter(i.authorityGovt))
        .where((i) => schoolLevelFilter.isEnabledInFilter(i.classLevel));

    return groupBy(filteredList, (obj) => obj.schoolTypeCode);
  }

  Map<dynamic, List<SchoolModel>> getSortedBySchoolType() {
    return groupBy(_schools, (obj) => obj.schoolTypeCode);
  }

  Map<dynamic, List<SchoolModel>> getSortedWithFilteringBySchoolLevel() {
    var filteredList = _schools
        .where((i) => stateFilter.isEnabledInFilter(i.districtCode))
        .where((i) => yearFilter.isEnabledInFilter(i.surveyYear.toString()))
        .where((i) => authorityFilter.isEnabledInFilter(i.authorityCode))
        .where((i) => ageFilter.isEnabledInFilter(i.ageGroup))
        .where((i) => govtFilter.isEnabledInFilter(i.authorityGovt))
        .where((i) => schoolTypeFilter.isEnabledInFilter(i.schoolTypeCode));

    return groupBy(filteredList, (obj) => obj.classLevel);
  }

  Map<dynamic, List<SchoolModel>> getSortedBySchoolLevel() {
    return groupBy(_schools, (obj) => obj.classLevel);
  }

  Map<dynamic, List<SchoolModel>> getSortedByAge(int type) {
    var filteredList = _schools.where((i) => ageFilter.isEnabledInFilter(i.ageGroup) && i.age > 0);

    switch (type) {
      case 0:
        break;
      case 1:
        filteredList = filteredList.where((i) => ["GK"].contains(i.classLevel));
        break;
      case 2:
        filteredList = filteredList.where((i) => ["G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8"].contains(i.classLevel));
        break;
      case 3:
        filteredList = filteredList.where((i) => ["G9", "G10", "G11", "G12"].contains(i.classLevel));
        break;
      case 4:
        filteredList = filteredList
            .where((i) => !["GK", "G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "G10", "G11", "G12"].contains(i.classLevel));
        break;
      default:
        break;
    }

    return groupBy(filteredList, (obj) => obj.ageGroup);
  }

  void _createFilters() {
    List<SchoolModel> _schoolsValidAge = _schools.where((i) => i.age > 0).toList();
    _filters = {
      'authority':
          Filter(List<String>.generate(_schools.length, (i) => _schools[i].authorityCode).toSet(), 'Authotity filter'),
      'state': Filter(List<String>.generate(_schools.length, (i) => _schools[i].districtCode).toSet(), 'State filter'),
      'schoolType': Filter(List<String>.generate(_schools.length, (i) => _schools[i].schoolTypeCode).toSet(),
          'Schools Enrollment by School type, \nState and Gender'),
      'age':
          Filter(List<String>.generate(_schoolsValidAge.length, (i) => _schoolsValidAge[i].ageGroup).toSet(), 'Schools Enrollment by Age'),
      'govt': Filter(List<String>.generate(_schools.length, (i) => _schools[i].authorityGovt).toSet(), 'Goverment filter'),
      'year':
          Filter(List<String>.generate(_schools.length, (i) => _schools[i].surveyYear.toString()).toSet(), 'Years filter'),
      'schoolLevel': Filter(List<String>.generate(_schools.length, (i) => _schools[i].classLevel).toSet(), 'Schools Levels filter'),
    };
    yearFilter.selectMax();
  }
}