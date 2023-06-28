import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'si_contact_ringtone_platform_interface.dart';

/// An implementation of [SiContactRingtonePlatform] that uses method channels.
class MethodChannelSiContactRingtone extends SiContactRingtonePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('si_contact_ringtone');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool?> requestContactsMusicAndStoragePermissions() async{
    final granted =
    await methodChannel.invokeMethod<bool>('requestContactsMusicAndStoragePermissions');
    return granted;
  }

  @override
  Future<String?> getContacts() async{
    final contacts = await methodChannel.invokeMethod<String>('getContacts');
    return contacts;
  }

  @override
  Future<bool?> setContactNameByNumber(String contactNumber, String newName ) async {
    final res = await methodChannel.invokeMethod<bool>('setContactNameByNumber',
    {"contactNumber":contactNumber,
      "newName": newName
    }
    );
    return res;
  }

  @override
  Future<bool?> setContactRingtoneByNumber(String contactNumber, String path ) async {
    final res = await methodChannel.invokeMethod<bool>('setRingtonByNumber',
        {"contactNumber":contactNumber,
          "path": path
        }
    );
    return res;
  }

  @override
  Future<String?> getAllMusicAndRingtones() async {
    final res = await methodChannel.invokeMethod<String>('getAllMusicAndRingtones');
    return res;
  }


}
