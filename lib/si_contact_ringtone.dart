
import 'si_contact_ringtone_platform_interface.dart';

class SiContactRingtone {
  Future<String?> getPlatformVersion() {
    return SiContactRingtonePlatform.instance.getPlatformVersion();
  }

  Future<String?> setContactNameByNumber(String newName, String contactNumber) {
   return SiContactRingtonePlatform.instance.setContactNameByNumber(newName, contactNumber);
  }
}
