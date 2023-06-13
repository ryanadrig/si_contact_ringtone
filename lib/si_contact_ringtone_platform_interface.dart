import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'si_contact_ringtone_method_channel.dart';

abstract class SiContactRingtonePlatform extends PlatformInterface {
  /// Constructs a SiContactRingtonePlatform.
  SiContactRingtonePlatform() : super(token: _token);

  static final Object _token = Object();

  static SiContactRingtonePlatform _instance = MethodChannelSiContactRingtone();

  /// The default instance of [SiContactRingtonePlatform] to use.
  ///
  /// Defaults to [MethodChannelSiContactRingtone].
  static SiContactRingtonePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SiContactRingtonePlatform] when
  /// they register themselves.
  static set instance(SiContactRingtonePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }


}
