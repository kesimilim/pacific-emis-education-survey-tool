import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

class CategoryGridWidget extends StatelessWidget {
  final List<String> _kCategoryData = <String>[
    "Schools",
    "Teachers",
    "Exams",
    "School Accreditations",
    "Indicators",
    "Budgets"
  ];

  @override
  Widget build(BuildContext context) {
    return GridView.count(
      physics: new NeverScrollableScrollPhysics(),
      crossAxisCount: 2,
      crossAxisSpacing: 24,
      mainAxisSpacing: 24,
      padding: EdgeInsets.all(16.0),
      childAspectRatio: 8.0 / 9.0,
      shrinkWrap: true,
      children: _kCategoryData
          .map(
            (data) => new Container(
                  margin: const EdgeInsets.only(left: 5.0),
                  child: Card(
                    shape: const RoundedRectangleBorder(
                      borderRadius: BorderRadius.all(Radius.circular(16.0)),
                    ),
                    child: InkWell(
                      
                      splashColor: Colors.blue.withAlpha(30),
                      onTap: () {
                        Navigator.pushNamed(context, '/$data');
                      },
                      child: ClipRect(
                        clipBehavior: Clip.hardEdge,
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: <Widget>[
                            SvgPicture.asset("images/icons/$data.svg",
                                width: 64, height: 64),
                            new Container(
                              margin: const EdgeInsets.only(left: 5.0),
                              child: Text(
                                data,
                                style: TextStyle(fontFamily: "NotoSans"),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ),
                  decoration: BoxDecoration(
                    boxShadow: <BoxShadow>[
                      new BoxShadow(
                        color: Color.fromRGBO(8, 36, 73, 0.4),
                        blurRadius: 16.0,
                        offset: new Offset(0.0, 16.0),
                      ),
                    ],
                  ),
                ),
          )
          .toList(),
    );
  }
}
