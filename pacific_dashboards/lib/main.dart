import 'package:flutter/material.dart';

import 'src/app.dart';
import 'src/ui/injector_widget.dart';

void main() async {
  var injector = InjectorWidget(child : App());
  await injector.init();
  runApp(injector);
}