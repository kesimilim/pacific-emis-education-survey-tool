import 'package:collection/collection.dart';
import 'package:pacific_dashboards/models/filter.dart';
import 'package:pacific_dashboards/models/lookups_model.dart';
import 'package:pacific_dashboards/models/model_with_lookups.dart';
import 'package:pacific_dashboards/models/school_model.dart';
import 'package:pacific_dashboards/res/strings/strings.dart';

class SchoolsModel extends ModelWithLookups {
  Map<String, Filter> _filters;
  List<SchoolModel> _schools;

  List<SchoolModel> get schools => _schools;

  Filter get yearFilter => _filters['year'];

  void updateYearFilter(Filter newFilter) {
    _filters['year'] = newFilter;
  }

  Filter get stateFilter => _filters['state'];

  void updateStateFilter(Filter newFilter) {
    _filters['state'] = newFilter;
  }

  Filter get authorityFilter => _filters['authority'];

  void updateAuthorityFilter(Filter newFilter) {
    _filters['authority'] = newFilter;
  }

  Filter get govtFilter => _filters['govt'];

  void updateGovtFilter(Filter newFilter) {
    _filters['govt'] = newFilter;
  }

  Filter get schoolTypeFilter => _filters['schoolType'];

  Filter get ageFilter => _filters['age'];

  Filter get schoolLevelFilter => _filters['schoolLevel'];

  void updateSchoolLevelFilter(Filter newFilter) {
    _filters['schoolLevel'] = newFilter;
  }

  SchoolsModel.fromJson(List parsedJson) {
    _schools = List<SchoolModel>();
    _schools = parsedJson.map((i) => SchoolModel.fromJson(i)).toList();
    _createFilters();
  }

  List toJson() {
    return _schools.map((i) => (i).toJson()).toList();
  }

  List<SchoolModel> get _filteredSchools => _schools
      .where((it) => yearFilter.isEnabledInFilter(it.surveyYear.toString()))
      .where((it) => authorityFilter.isEnabledInFilter(it.authorityCode))
      .where((it) => govtFilter.isEnabledInFilter(it.authorityGovt))
      .where((it) => schoolTypeFilter.isEnabledInFilter(it.schoolTypeCode))
      .where((it) => ageFilter.isEnabledInFilter(it.ageGroup))
      .where((it) => schoolLevelFilter.isEnabledInFilter(it.classLevel))
      .toList();

  Map<String, List<SchoolModel>> getSortedWithFiltersByState() {
    return groupBy(
        _filteredSchools, (obj) => lookupsModel.getFullState(obj.districtCode));
  }

  Map<String, List<SchoolModel>> getSortedByState() {
    return groupBy(
        _schools, (obj) => lookupsModel.getFullState(obj.districtCode));
  }

  Map<String, List<SchoolModel>> getSortedWithFiltersByAuthority() {
    return groupBy(_filteredSchools,
        (obj) => lookupsModel.getFullAuthority(obj.authorityCode));
  }

  Map<String, List<SchoolModel>> getSortedByAuthority() {
    return groupBy(
        _schools, (obj) => lookupsModel.getFullAuthority(obj.authorityCode));
  }

  Map<String, List<SchoolModel>> getSortedWithFiltersByGovt() {
    return groupBy(
        _filteredSchools, (obj) => lookupsModel.getFullGovt(obj.authorityGovt));
  }

  Map<String, List<SchoolModel>> getSortedByGovt() {
    return groupBy(
        _schools, (obj) => lookupsModel.getFullGovt(obj.authorityGovt));
  }

  List<String> getDistrictCodeKeysList() {
    return groupBy<SchoolModel, String>(_schools, (obj) => obj.districtCode)
        .keys
        .toList();
  }

  Map<String, List<SchoolModel>> getSortedWithFilteringBySchoolType() {
    return groupBy(_filteredSchools, (obj) => obj.schoolTypeCode);
  }

  Map<String, List<SchoolModel>> getSortedBySchoolType() {
    return groupBy(_schools, (obj) => obj.schoolTypeCode);
  }

  Map<String, List<SchoolModel>> getSortedWithFilteringBySchoolLevel() {
    return groupBy(_filteredSchools, (obj) => obj.classLevel);
  }

  Map<String, List<SchoolModel>> getSortedBySchoolLevel() {
    return groupBy(_schools, (obj) => obj.classLevel);
  }

  Map<String, List<SchoolModel>> getGroupedByAgeFileteredByEducationLevel(
      EducationLevel level) {
    var filteredList = _filteredSchools;

    switch (level) {
      case EducationLevel.all:
        break;
      case EducationLevel.earlyChildhood:
        filteredList =
            filteredList.where((i) => ["GK"].contains(i.classLevel)).toList();
        break;
      case EducationLevel.primary:
        filteredList = filteredList
            .where((i) => ["G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8"]
                .contains(i.classLevel))
            .toList();
        break;
      case EducationLevel.secondary:
        filteredList = filteredList
            .where((i) => ["G9", "G10", "G11", "G12"].contains(i.classLevel))
            .toList();
        break;
      case EducationLevel.postSecondary:
        filteredList = filteredList
            .where((i) => ![
                  "GK",
                  "G1",
                  "G2",
                  "G3",
                  "G4",
                  "G5",
                  "G6",
                  "G7",
                  "G8",
                  "G9",
                  "G10",
                  "G11",
                  "G12"
                ].contains(i.classLevel))
            .toList();
        break;
    }

    return groupBy(filteredList, (obj) => obj.ageGroup);
  }

  void _createFilters() {
    List<SchoolModel> _schoolsValidAge =
        _schools.where((i) => i.age > 0).toList();
    _filters = {
      'authority': Filter(
          List<String>.generate(
              _schools.length, (i) => _schools[i].authorityCode).toSet(),
          AppLocalizations.filterByAuthority,
          this,
          LookupsModel.LOOKUPS_KEY_AUTHORITY),
      'state': Filter(
          List<String>.generate(
              _schools.length, (i) => _schools[i].districtCode).toSet(),
          AppLocalizations.filterByState,
          this,
          LookupsModel.LOOKUPS_KEY_STATE),
      'schoolType': Filter(
          List<String>.generate(
              _schools.length, (i) => _schools[i].schoolTypeCode).toSet(),
          'Schools Enrollment by School type, \nState and Gender',
          this,
          LookupsModel.LOOKUPS_KEY_GOVT),
      'age': Filter(
          List<String>.generate(
                  _schoolsValidAge.length, (i) => _schoolsValidAge[i].ageGroup)
              .toSet(),
          'Schools Enrollment by Age',
          this,
          LookupsModel.LOOKUPS_KEY_NO_KEY),
      'govt': Filter(
          List<String>.generate(
              _schools.length, (i) => _schools[i].authorityGovt).toSet(),
          AppLocalizations.filterByGovernment,
          this,
          LookupsModel.LOOKUPS_KEY_NO_KEY),
      'year': Filter(
          List<String>.generate(
                  _schools.length, (i) => _schools[i].surveyYear.toString())
              .reversed
              .toSet(),
          AppLocalizations.filterByYear,
          this,
          LookupsModel.LOOKUPS_KEY_NO_KEY),
      'schoolLevel': Filter(
          List<String>.generate(_schools.length, (i) => _schools[i].classLevel)
              .toSet(),
          AppLocalizations.filterByClassLevel,
          this,
          LookupsModel.LOOKUPS_KEY_NO_KEY),
    };
    yearFilter.selectMax();
  }
}

enum EducationLevel { all, earlyChildhood, primary, secondary, postSecondary }
