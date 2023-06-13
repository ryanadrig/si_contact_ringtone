
import 'package:si_contact_ringtone/si_contact_ringtone_method_channel.dart';

import 'si_contact_ringtone_platform_interface.dart';

class SiContactRingtone {


  Future<String?> getPlatformVersion() {
    return SiContactRingtonePlatform.instance.getPlatformVersion();
  }

  Future<String?> setContactNameByNumber(
      String contactName,
      String newNumber) {
    return SiContactRingtonePlatform.instance.setContactNameByNumber(
         contactName,
        newNumber);
  }

}
