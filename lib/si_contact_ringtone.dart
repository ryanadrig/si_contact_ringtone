
import 'si_contact_ringtone_platform_interface.dart';

class SiContactRingtone {
  Future<String?> getPlatformVersion() {
    return SiContactRingtonePlatform.instance.getPlatformVersion();
  }
}
