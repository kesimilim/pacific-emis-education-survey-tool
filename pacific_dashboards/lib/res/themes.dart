import 'package:flutter/material.dart';
import 'package:pacific_dashboards/res/colors.dart';
import 'package:charts_flutter/flutter.dart' as charts
    show TextStyleSpec, Color;

final ThemeData appTheme = ThemeData(
  brightness: Brightness.light,
  primaryColor: AppColors.kBlue,
  accentColor: AppColors.kEndeavour,
  fontFamily: 'NotoSans',
  iconTheme: const IconThemeData(color: Colors.white),
  splashColor: AppColors.kRipple,
  textTheme: const TextTheme(
      headline2: const TextStyle(
        color: Colors.white,
        fontSize: 28.0,
        fontWeight: FontWeight.bold,
        fontStyle: FontStyle.normal,
      ),
      headline3: const TextStyle(
        color: Colors.white,
        fontSize: 24.0,
        fontWeight: FontWeight.bold,
        fontStyle: FontStyle.normal,
      ),
      headline4: const TextStyle(
        color: AppColors.kTextMain,
        fontSize: 16.0,
        fontWeight: FontWeight.bold,
        fontStyle: FontStyle.normal,
      ),
      headline5: const TextStyle(
        color: AppColors.kNevada,
        fontSize: 14.0,
        fontWeight: FontWeight.bold,
        fontStyle: FontStyle.normal,
      ),
      headline6: const TextStyle(
        color: Colors.white,
        fontSize: 18.0,
        fontWeight: FontWeight.normal,
        fontStyle: FontStyle.normal,
      ),
      subtitle1: const TextStyle(
        color: AppColors.kTextMain,
        fontSize: 14.0,
        fontWeight: FontWeight.normal,
        fontStyle: FontStyle.normal,
      ),
      button: const TextStyle(
        color: AppColors.kTextMain,
        fontSize: 16.0,
        fontWeight: FontWeight.normal,
        fontStyle: FontStyle.normal,
      ),
      subtitle2: const TextStyle(
        color: AppColors.kTextMain,
        fontSize: 12.0,
        fontWeight: FontWeight.normal,
        fontStyle: FontStyle.normal,
      ),
      overline: const TextStyle(
        color: AppColors.kNevada,
        fontSize: 10.0,
        fontWeight: FontWeight.normal,
        fontStyle: FontStyle.normal,
        letterSpacing: 0.0,
      ),
      bodyText1: const TextStyle(
        color: AppColors.kEndeavour,
        fontSize: 14.0,
        fontWeight: FontWeight.w500,
        fontStyle: FontStyle.normal,
        fontFamily: 'Roboto',
      ),
      bodyText2: const TextStyle(
        color: AppColors.kNevada,
        fontSize: 14.0,
        fontStyle: FontStyle.italic
      )),
  bottomSheetTheme: const BottomSheetThemeData(
    backgroundColor: Colors.transparent,
  ),
);

const charts.TextStyleSpec largeChartsDomain = const charts.TextStyleSpec(
  fontFamily: 'NotoSans',
  color: charts.Color(a: 255, r: 99, g: 105, b: 109),
  fontSize: 14,
);

extension ChartsTextStyle on TextStyle {
  charts.TextStyleSpec get chartsSpec => charts.TextStyleSpec(
        fontFamily: this.fontFamily,
        color: this.color.chartsColor,
        fontSize: this.fontSize.round(),
        lineHeight: this.height,
      );
}