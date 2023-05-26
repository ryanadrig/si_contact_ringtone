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
}
