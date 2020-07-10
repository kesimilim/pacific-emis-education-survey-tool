import 'package:pacific_dashboards/pages/exams/exams_view_model.dart';
import 'package:pacific_dashboards/pages/home/home_view_model.dart';
import 'package:pacific_dashboards/pages/school_accreditation/school_accreditation_view_model.dart';
import 'package:pacific_dashboards/pages/schools/schools_view_model.dart';
import 'package:pacific_dashboards/pages/teachers/teachers_view_model.dart';
import 'package:pacific_dashboards/service_locator.dart';

class ViewModelFactory {

  static ViewModelFactory _instance;

  static ViewModelFactory get instance {
    if (_instance == null) {
      _instance = ViewModelFactory._();
    }
    return _instance;
  }

  ViewModelFactory._();

  HomeViewModel get home {
    return HomeViewModel(
        globalSettings: serviceLocator.globalSettings,
        remoteConfig: serviceLocator.remoteConfig,
    );
  }

  SchoolsViewModel get schools {
    return SchoolsViewModel(
      repository: serviceLocator.repository,
      remoteConfig: serviceLocator.remoteConfig,
      globalSettings: serviceLocator.globalSettings,
    );
  }

  TeachersViewModel get teachers {
    return TeachersViewModel(
      repository: serviceLocator.repository,
      remoteConfig: serviceLocator.remoteConfig,
      globalSettings: serviceLocator.globalSettings,
    );
  }

  SchoolAccreditationViewModel get schoolAccreditation {
    return SchoolAccreditationViewModel(
      repository: serviceLocator.repository,
      remoteConfig: serviceLocator.remoteConfig,
      globalSettings: serviceLocator.globalSettings,
    );
  }

  ExamsViewModel get exams {
    return ExamsViewModel(
      repository: serviceLocator.repository,
      remoteConfig: serviceLocator.remoteConfig,
      globalSettings: serviceLocator.globalSettings,
    );
  }
}