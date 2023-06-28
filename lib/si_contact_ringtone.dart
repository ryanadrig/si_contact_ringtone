
import 'si_contact_ringtone_platform_interface.dart';

class SiContactRingtone {
  Future<String?> getPlatformVersion() {
    return SiContactRingtonePlatform.instance.getPlatformVersion();
  }

  Future<bool?> requestContactsMusicAndStoragePermissions(){
    return SiContactRingtonePlatform.instance.requestContactsMusicAndStoragePermissions();
  }

  Future<String?> getContacts() {
    return SiContactRingtonePlatform.instance.getContacts();
  }

  Future<bool?> setContactNameByNumber( String contactNumber, String newName,) {
   return SiContactRingtonePlatform.instance.setContactNameByNumber(contactNumber,newName );
  }


  Future<bool?> setContactRingtoneByNumber( String contactNumber, String path,) {
    return SiContactRingtonePlatform.instance.setContactRingtoneByNumber(contactNumber,path );
  }

  Future<String?> getAllMusicAndRingtones() {
    return SiContactRingtonePlatform.instance.getAllMusicAndRingtones();
  }

}
