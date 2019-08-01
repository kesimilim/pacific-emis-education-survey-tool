import 'package:flutter/material.dart';
import 'package:pacific_dashboards/src/ui/schools_ui/SchoolsPage.dart';
import 'package:pacific_dashboards/src/ui/splash_ui/SplashPage.dart';

import 'ui/home_ui/HomePage.dart';
import 'ui/InjectorWidget.dart';
import 'ui/teaches_ui/TeachersPage.dart';

class App extends StatelessWidget {
  final _appName = 'Custom Charts';

  @override
  Widget build(BuildContext context) {
    final injector = InjectorWidget.of(context);
    return MaterialApp(
      title: _appName,
      theme: ThemeData(
        brightness: Brightness.light,
        primaryColor: Colors.white,
        accentColor: Colors.deepOrangeAccent[100],
        fontFamily: 'Montserrat',
        textTheme: TextTheme(
          headline: TextStyle(fontSize: 72.0, fontWeight: FontWeight.bold),
          title: TextStyle(fontSize: 36.0, fontStyle: FontStyle.italic),
          body1: TextStyle(fontSize: 14.0, fontFamily: 'Hind'),
        ),
      ),
      initialRoute: "/",
      routes: {
        "/": (context) =>
            SplashPage(globalSettings: injector.getGlobalSettings()),
        "/Home": (context) =>
            HomePage(globalSettings: injector.getGlobalSettings()),
        "/Budgets": (context) => Text("Budgets"),
        "/Exams": (context) => Text("Exams"),
        "/Indicators": (context) => Text("Indicators"),
        "/School Accreditations": (context) => Text("School Accreditations"),
        "/Schools": (context) =>
            SchoolsPage(bloc: injector.getSchoolsBloc()),
        "/Teachers": (context) =>
            TeachersPage(bloc: injector.getTeachersBloc()),
      },
    );
  }
}