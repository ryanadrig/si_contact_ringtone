
import 'package:si_contact_ringtone/si_contact_ringtone_method_channel.dart';

import 'si_contact_ringtone_platform_interface.dart';

class SiContactRingtone {
  

  Future<String?> getPlatformVersion() {
    return SiContactRingtonePlatform.instance.getPlatformVersion();
  }

  static Future<void> setNavigationBarColor(
      Color color, {
        bool animate = false,
      }) =>

      mcscr.methodChannel.invokeMethod('setnavigationbarcolor', {
        'color': color.value,
        'animate': animate,
      });

}
