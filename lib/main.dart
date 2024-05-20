import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Fruit Selection App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const UserInputScreen(),
    );
  }
}

class UserInputScreen extends StatefulWidget {
  const UserInputScreen({super.key});

  @override
  _UserInputScreenState createState() => _UserInputScreenState();
}

class _UserInputScreenState extends State<UserInputScreen> {
  final _formKey = GlobalKey<FormState>();
  String name = '';
  String addressLine1 = '';
  String city = '';
  String state = '';
  String country = '';

  static const platform = MethodChannel('com.example.app/tts');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('User Input')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                decoration: const InputDecoration(labelText: 'Name'),
                onSaved: (value) => name = value!,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: 'Address Line 1'),
                onSaved: (value) => addressLine1 = value!,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: 'City'),
                onSaved: (value) => city = value!,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: 'State'),
                onSaved: (value) => state = value!,
              ),
              TextFormField(
                decoration: const InputDecoration(labelText: 'Country'),
                onSaved: (value) => country = value!,
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  _formKey.currentState?.save();
                  _sendDataToNative();
                },
                child: const Text('Submit'),
              ),
              ElevatedButton(
                onPressed: () {
                  _startNativeFruitSelection();
                },
                child: const Text('Select Fruits'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _sendDataToNative() async {
    final Map<String, String> data = {
      'name': name,
      'addressLine1': addressLine1,
      'city': city,
      'state': state,
      'country': country,
    };
    try {
      await platform.invokeMethod('speak', data);
    } on PlatformException catch (e) {
      print("Failed to invoke method: '${e.message}'.");
    }
  }

  Future<void> _startNativeFruitSelection() async {
    try {
      await platform.invokeMethod('startFruitSelection');
    } on PlatformException catch (e) {
      print("Failed to start fruit selection: '${e.message}'.");
    }
  }
}

class FruitDisplayScreen extends StatelessWidget {
  final List<String> fruits;
  const FruitDisplayScreen({super.key, required this.fruits});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Selected Fruits')),
      body: ListView.builder(
        itemCount: fruits.length,
        itemBuilder: (context, index) {
          return ListTile(
            title: Text(fruits[index]),
            leading:
                Image.asset('assets/images/${fruits[index].toLowerCase()}.png'),
          );
        },
      ),
    );
  }
}
