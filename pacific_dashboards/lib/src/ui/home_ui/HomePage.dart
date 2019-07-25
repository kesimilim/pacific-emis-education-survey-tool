import 'package:flutter/material.dart';
import 'package:pacific_dashboards/src/ui/splash_ui/SplashPage.dart';
import 'package:shared_preferences/shared_preferences.dart';
import "../CategoryGridWidget.dart";

class HomePage extends StatefulWidget {
  final SharedPreferences sharedPreferences;

  @override
  _HomePageState createState() => new _HomePageState();

  HomePage({Key key, this.sharedPreferences,}): super(key: key);
}

class _HomePageState extends State<HomePage> {
  final String _kMarshallIslands = "Marshall Islands";
  final String _kFederatedStateOfMicronesia = "Federated States of Micronesia";
  String _currentCountry;

  @override
  void initState() {
    super.initState();
    _currentCountry = SplashPage(sharedPreferences: widget.sharedPreferences).currentCountry;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomPadding: true,
      body: new Container(
        decoration: BoxDecoration(color: Colors.white),
        child: new ListView(children: <Widget>[
          Container(
            height: 80,
            alignment: Alignment.centerRight,
            child: _buildChooseCountry(context),
          ),
          Container(
              height: 160,
              width: 160,
              child: Image.asset("images/logos/$_currentCountry.png")),
          Container(
            height: 96,
            width: 266,
            alignment: Alignment.center,
            child: Center(
                child: Text(
              _currentCountry,
              textAlign: TextAlign.center,
              style: TextStyle(
                  fontStyle: FontStyle.normal,
                  fontWeight: FontWeight.w700,
                  fontSize: 24,
                  fontFamily: "NotoSans"),
            )),
          ),
          Container(
              height: 500,
              width: 328,
              alignment: Alignment.center,
              child: CategoryGridWidget())
        ]),
      ),
    );
  }

  _buildChooseCountry(BuildContext context) {
    return FlatButton(
      color: Colors.white,
      textColor: Color.fromRGBO(26, 129, 204, 0.8),
      disabledColor: Colors.grey,
      disabledTextColor: Colors.black,
      padding: EdgeInsets.all(8.0),
      splashColor: Colors.lightBlue,
      onPressed: () {
        _showDialog(context);
      },
      child: Text(
        "Change country",
        style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w700),
      ),
    );
  }

  void _showDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Container(
          width: 280,
          height: 244,
          child: Container(
            width: 280,
            height: 244,
            child: AlertDialog(
              title: Text(
                "Choice country",
                style: TextStyle(
                    fontStyle: FontStyle.normal,
                    fontWeight: FontWeight.w700,
                    fontSize: 24,
                    fontFamily: "NotoSans"),
              ),
              content: Container(
                height: 200,
                width: 400,
                child: Column(children: <Widget>[
                  Expanded(
                    child: InkWell(
                      splashColor: Colors.blue.withAlpha(30),
                      onTap: () {
                        setState(() {
                          _setCurrentCountry(
                              "$_kFederatedStateOfMicronesia", context);
                        });
                      },
                      child: Row(
                        children: <Widget>[
                          Expanded(
                              child: Image.asset(
                                  "images/logos/$_kFederatedStateOfMicronesia.png",
                                  width: 40,
                                  height: 40)),
                          Expanded(
                            child: Text("$_kFederatedStateOfMicronesia",
                                style: TextStyle(fontFamily: "NotoSans")),
                          ),
                        ],
                      ),
                    ),
                  ),
                  Expanded(
                      child: InkWell(
                    splashColor: Colors.blue.withAlpha(30),
                    onTap: () {
                      setState(() {
                        _setCurrentCountry("$_kMarshallIslands", context);
                      });
                    },
                    child: Row(
                      children: <Widget>[
                        Expanded(
                          child: Image.asset(
                            "images/logos/$_kMarshallIslands.png",
                            width: 40,
                            height: 40,
                          ),
                        ),
                        Expanded(
                          child: Text(_kMarshallIslands,
                              style: TextStyle(fontFamily: "NotoSans")),
                        ),
                      ],
                    ),
                  ))
                ]),
              ),
            ),
          ),
        );
      },
    );
  }

  _setCurrentCountry(String country, BuildContext context) async {
    await widget.sharedPreferences.setString("country", country);
    setState(() {
      _currentCountry = country;
    });
    Navigator.of(context).pop();
  }

  init() async {

  }
}