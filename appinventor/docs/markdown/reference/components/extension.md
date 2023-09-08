---
layout: documentation
title: Extension
---

[&laquo; Back to index](index.html)
# Extension

Table of Contents:

* [AlarmManager](#AlarmManager)
* [BatteryOptimizations](#BatteryOptimizations)
* [CheckboxUtils](#CheckboxUtils)
* [DeviceInfo](#DeviceInfo)
* [DictionaryUtils](#DictionaryUtils)
* [DynamicComponents](#DynamicComponents)
* [FileUtils](#FileUtils)
* [GLCM](#GLCM)
* [KCalCalculator](#KCalCalculator)
* [LabelUtils](#LabelUtils)
* [ListUtils](#ListUtils)
* [MarchUtils](#MarchUtils)
* [OTPSender](#OTPSender)
* [OTPUtils](#OTPUtils)
* [OneSignalScheduler](#OneSignalScheduler)
* [QRGenerator](#QRGenerator)
* [SimpleBluetooth](#SimpleBluetooth)
* [SimpleButton](#SimpleButton)
* [SimpleDateAndTime](#SimpleDateAndTime)
* [SimpleFirebase](#SimpleFirebase)
* [SimpleMenu](#SimpleMenu)
* [SimplePedometer](#SimplePedometer)
* [SimplePedometer_v1](#SimplePedometer_v1)
* [SimplePedometer_v2](#SimplePedometer_v2)
* [SimplePedometer_v3](#SimplePedometer_v3)
* [SimpleSpreadsheet](#SimpleSpreadsheet)
* [StringFunctions](#StringFunctions)
* [Task](#Task)
* [TestBackground](#TestBackground)
* [TextBoxUtils](#TextBoxUtils)
* [Utils](#Utils)

## AlarmManager  {#AlarmManager}

Component for AlarmManager



### Properties  {#AlarmManager-Properties}

{:.properties}
None


### Events  {#AlarmManager-Events}

{:.events}
None


### Methods  {#AlarmManager-Methods}

{:.methods}
None


## BatteryOptimizations  {#BatteryOptimizations}

Component for BatteryOptimizations



### Properties  {#BatteryOptimizations-Properties}

{:.properties}

{:id="BatteryOptimizations.PackageName" .text} *PackageName*
: Package name of application.

### Events  {#BatteryOptimizations-Events}

{:.events}
None


### Methods  {#BatteryOptimizations-Methods}

{:.methods}

{:id="BatteryOptimizations.AskIgnoreOptimizations" class="method"} <i/> AskIgnoreOptimizations()
: Ask for ignoring battery optimizations

{:id="BatteryOptimizations.IsIgnoring" class="method returns boolean"} <i/> IsIgnoring()
: To determine if an application is already ignoring optimizations.

## CheckboxUtils  {#CheckboxUtils}

Component for CheckboxUtils



### Properties  {#CheckboxUtils-Properties}

{:.properties}
None


### Events  {#CheckboxUtils-Events}

{:.events}
None


### Methods  {#CheckboxUtils-Methods}

{:.methods}

{:id="CheckboxUtils.GetAllChecked" class="method returns list"} <i/> GetAllChecked()
: Get all checked state of checkboxes as list.

{:id="CheckboxUtils.Initialize" class="method"} <i/> Initialize(*checkboxes*{:.list},*min*{:.number},*max*{:.number})
: To initialize checkboxes.

{:id="CheckboxUtils.Listen" class="method"} <i/> Listen(*component*{:.component})
: To listen changed.

## DeviceInfo  {#DeviceInfo}

Component for DeviceInfo



### Properties  {#DeviceInfo-Properties}

{:.properties}
None


### Events  {#DeviceInfo-Events}

{:.events}

{:id="DeviceInfo.Failed"} Failed()
: Occurs when failed obtaining data.

{:id="DeviceInfo.GotData"} GotData()
: Occurs when obtained data.

### Methods  {#DeviceInfo-Methods}

{:.methods}

{:id="DeviceInfo.Get" class="method returns text"} <i/> Get(*data*{:.text})
: To get IP address.

{:id="DeviceInfo.Start" class="method"} <i/> Start()
: To start getting info of device.

## DictionaryUtils  {#DictionaryUtils}

Component for DictionaryUtils



### Properties  {#DictionaryUtils-Properties}

{:.properties}
None


### Events  {#DictionaryUtils-Events}

{:.events}
None


### Methods  {#DictionaryUtils-Methods}

{:.methods}

{:id="DictionaryUtils.ListKeys" class="method returns list"} <i/> ListKeys(*dictionary*{:.dictionary})
: To get list of keys.

{:id="DictionaryUtils.ListToDictionary" class="method returns dictionary"} <i/> ListToDictionary(*list*{:.list},*separator*{:.text},*keys*{:.list})
: To make dictionary with list value from list.

{:id="DictionaryUtils.SortByListIndex" class="method returns dictionary"} <i/> SortByListIndex(*dictionary*{:.dictionary},*key*{:.text},*list*{:.list})
: Sort list by index of given list in dictionary value by key.

{:id="DictionaryUtils.SortDate" class="method returns dictionary"} <i/> SortDate(*dictionary*{:.dictionary},*key*{:.text},*pattern*{:.text})
: Sort list of date in dictionary value by key.

{:id="DictionaryUtils.SortDigit" class="method returns dictionary"} <i/> SortDigit(*dictionary*{:.dictionary},*key*{:.text})
: Sort list of integer in dictionary value by key.

{:id="DictionaryUtils.SortLetter" class="method returns dictionary"} <i/> SortLetter(*dictionary*{:.dictionary},*key*{:.text})
: Sort list of string in dictionary value by key.

{:id="DictionaryUtils.StringToDictionary" class="method returns dictionary"} <i/> StringToDictionary(*string*{:.text},*separator*{:.text},*keys*{:.list})
: To make dictionary from string.

## DynamicComponents  {#DynamicComponents}

Component for DynamicComponents



### Properties  {#DynamicComponents-Properties}

{:.properties}

{:id="DynamicComponents.Thread" .text .wo .do} *Thread*
: Property for Thread

### Events  {#DynamicComponents-Events}

{:.events}

{:id="DynamicComponents.AllRemoved"} AllRemoved()
: Occurs when all components has been removed.

{:id="DynamicComponents.ComponentCreated"} ComponentCreated(*component*{:.component},*id*{:.text},*type*{:.text})
: Occurs when component has been created.

{:id="DynamicComponents.SchemaCreated"} SchemaCreated(*name*{:.text},*parameters*{:.list})
: Occurs when schema has/mostly finished component creation.

### Methods  {#DynamicComponents-Methods}

{:.methods}

{:id="DynamicComponents.ChangeId" class="method"} <i/> ChangeId(*id*{:.text},*newId*{:.text})
: Assign a new ID to a previously created dynamic component.

{:id="DynamicComponents.Create" class="method"} <i/> Create(*in*{:.component},*component*{:.any},*id*{:.text})
: Create a new dynamic component.

{:id="DynamicComponents.GenerateID" class="method returns text"} <i/> GenerateID()
: Generate a random id to create a component.

{:id="DynamicComponents.GetAllIds" class="method returns list"} <i/> GetAllIds()
: Returns all IDs of created components.

{:id="DynamicComponents.GetComponent" class="method returns any"} <i/> GetComponent(*id*{:.text})
: Returns the component associated with the specified ID.

{:id="DynamicComponents.GetComponentMeta" class="method returns dictionary"} <i/> GetComponentMeta(*component*{:.component})
: Get meta data about the specified component.

{:id="DynamicComponents.GetEventMeta" class="method returns dictionary"} <i/> GetEventMeta(*component*{:.component})
: Get meta data about events for the specified component.

{:id="DynamicComponents.GetFunctionMeta" class="method returns dictionary"} <i/> GetFunctionMeta(*component*{:.component})
: Get meta data about functions for the specified component.

{:id="DynamicComponents.GetId" class="method returns text"} <i/> GetId(*component*{:.component})
: Return id of the specified component.

{:id="DynamicComponents.GetOrder" class="method returns number"} <i/> GetOrder(*component*{:.component})
: Return the position of the specified component according to its parent view. Index begins at one.

{:id="DynamicComponents.GetProperty" class="method returns any"} <i/> GetProperty(*component*{:.component},*name*{:.text})
: Get properties value.

{:id="DynamicComponents.GetPropertyMeta" class="method returns dictionary"} <i/> GetPropertyMeta(*component*{:.component})
: Get meta data about properties for the specified component.

{:id="DynamicComponents.Invoke" class="method returns any"} <i/> Invoke(*component*{:.component},*name*{:.text},*parameters*{:.list})
: Invokes a method with parameters.

{:id="DynamicComponents.LastUsedId" class="method returns any"} <i/> LastUsedId()
: Return last used id.

{:id="DynamicComponents.Move" class="method"} <i/> Move(*layout*{:.component},*component*{:.component})
: Moves the specified component to specified view.

{:id="DynamicComponents.Remove" class="method"} <i/> Remove(*id*{:.text})
: Remove the component with the specified id from layout/screen.

{:id="DynamicComponents.RemoveAll" class="method"} <i/> RemoveAll()
: Remove all component from layout/screen.

{:id="DynamicComponents.SchemaCreate" class="method"} <i/> SchemaCreate(*in*{:.component},*template*{:.text},*parameters*{:.list})
: Use a JSON Object to create dynamic components.

{:id="DynamicComponents.SetOrder" class="method"} <i/> SetOrder(*component*{:.component},*index*{:.number})
: Sets the order of the specified component according to its parent view.

{:id="DynamicComponents.SetProperties" class="method"} <i/> SetProperties(*id*{:.text},*properties*{:.dictionary})
: Set nultiple properties of the specified component, including those only available from the designer.

{:id="DynamicComponents.SetProperty" class="method"} <i/> SetProperty(*id*{:.text},*name*{:.text},*value*{:.any})
: Set a property of the specified component, including those only available from the designer.

{:id="DynamicComponents.isDynamic" class="method returns boolean"} <i/> isDynamic(*component*{:.component})
: Return if specified component was created by DynamicComponents.

## FileUtils  {#FileUtils}

Component for FileUtils



### Properties  {#FileUtils-Properties}

{:.properties}

{:id="FileUtils.ReadPermission" .boolean .wo} *ReadPermission*
: To enable read access to file storage outside of the app-specific directories.

{:id="FileUtils.WritePermission" .boolean .wo} *WritePermission*
: To enable write access to file storage outside of the app-specific directories.

### Events  {#FileUtils-Events}

{:.events}

{:id="FileUtils.CacheCleared"} CacheCleared(*path*{:.text},*directoryName*{:.text})
: An event that occurrs when cache has been cleared.

{:id="FileUtils.DirectoryCreated"} DirectoryCreated(*path*{:.text},*directoryName*{:.text})
: An event that occurrs when directory has been created.

{:id="FileUtils.DirectoryRemoved"} DirectoryRemoved(*path*{:.text},*directoryName*{:.text})
: An event that occurrs when directory has been removed.

{:id="FileUtils.ErrorOccurred"} ErrorOccurred(*messages*{:.any})
: An event that occurrs when error occurred.

{:id="FileUtils.GotLists"} GotLists(*list*{:.list})
: An event that occurrs when got list of file and directory.

{:id="FileUtils.Moved"} Moved(*fileName*{:.any})
: An event that occurrs when file is moved.

### Methods  {#FileUtils-Methods}

{:.methods}

{:id="FileUtils.ClearCache" class="method"} <i/> ClearCache(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*directoryName*{:.text})
: To clear cache directory.

{:id="FileUtils.Exists" class="method returns boolean"} <i/> Exists(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*path*{:.text})
: Tests whether the path exists in the given scope.

{:id="FileUtils.GetApplicationPath" class="method returns text"} <i/> GetApplicationPath()
: To get path of app.

{:id="FileUtils.GetExternalStoragePath" class="method returns text"} <i/> GetExternalStoragePath()
: To get external storage path.

{:id="FileUtils.GetName" class="method returns text"} <i/> GetName(*path*{:.text})
: To get name of file or directory.

{:id="FileUtils.GetSize" class="method returns number"} <i/> GetSize(*sizeIn*{:.com.marchtech.FileUtils.helpers.SizeEnum},*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*path*{:.text})
: To get size of file or directory.

{:id="FileUtils.GetTotalFile" class="method returns number"} <i/> GetTotalFile(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*path*{:.text},*recursive*{:.boolean})
: To get total file and directory.

{:id="FileUtils.ListFileAndDirectory" class="method"} <i/> ListFileAndDirectory(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*directoryName*{:.text})
: Get a list of files and directories in the given directory.

{:id="FileUtils.MakeDirectory" class="method"} <i/> MakeDirectory(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*directoryName*{:.text})
: Create a new directory for storing files.

{:id="FileUtils.MakeFullPath" class="method returns text"} <i/> MakeFullPath(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*path*{:.text})
: Converts the scope and path into a single string for other components.

{:id="FileUtils.MoveIntoPictures" class="method"} <i/> MoveIntoPictures(*fromFileName*{:.text},*toFileName*{:.text})
: To move file into Pictures directory.

{:id="FileUtils.RemoveDirectory" class="method"} <i/> RemoveDirectory(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*directoryName*{:.text},*recursive*{:.boolean})
: Remove a directory from the file system.

## GLCM  {#GLCM}

Component for GLCM



### Properties  {#GLCM-Properties}

{:.properties}

{:id="GLCM.Mode" .text .wo .bo} *Mode*
: To get mode.

{:id="GLCM.Picture" .text .wo} *Picture*
: To set picture to analyze.

### Events  {#GLCM-Events}

{:.events}

{:id="GLCM.ErrorOccurred"} ErrorOccurred(*messages*{:.text})
: An event that occurs when errors has occured.

{:id="GLCM.Finished"} Finished(*result*{:.dictionary})
: An event that occurs when GLCM has been calculated.

{:id="GLCM.OnNextStep"} OnNextStep(*step*{:.text})
: An event that occurs when Step has been finished.

### Methods  {#GLCM-Methods}

{:.methods}

{:id="GLCM.Next1" class="method"} <i/> Next1()
: Next step from start.

{:id="GLCM.Next2" class="method"} <i/> Next2()
: Next step from next1.

{:id="GLCM.Next3" class="method"} <i/> Next3()
: Next step from next2.

{:id="GLCM.Next4" class="method"} <i/> Next4()
: Next step from next3 before finished.

{:id="GLCM.SetMode" class="method"} <i/> SetMode(*mode*{:.list})
: To set result of GLCM method.

{:id="GLCM.Start" class="method"} <i/> Start(*distance*{:.number})
: Start GLCM Method

## KCalCalculator  {#KCalCalculator}

Component for KCalCalculator



### Properties  {#KCalCalculator-Properties}

{:.properties}

{:id="KCalCalculator.Activity" .text} *Activity*
: Set the activity level.

{:id="KCalCalculator.ActivityElements" .list .bo} *ActivityElements*
: Set the activity level elements.

{:id="KCalCalculator.ActivityIndex" .number .bo} *ActivityIndex*
: Set the activity level with index. range: 1 - 3

{:id="KCalCalculator.Age" .number} *Age*
: Set the age.

{:id="KCalCalculator.Gender" .text} *Gender*
: Set the gender.

{:id="KCalCalculator.GenderIndex" .number .bo} *GenderIndex*
: Set the gender with index. value 1 or 2

{:id="KCalCalculator.Height" .number} *Height*
: Set the height in centimeters.

{:id="KCalCalculator.Weight" .number} *Weight*
: Set the weight in kilograms.

### Events  {#KCalCalculator-Events}

{:.events}
None


### Methods  {#KCalCalculator-Methods}

{:.methods}

{:id="KCalCalculator.GetActivity" class="method returns number"} <i/> GetActivity()
: Method for GetActivity

{:id="KCalCalculator.GetBBI" class="method returns number"} <i/> GetBBI()
: Get BBI (Berat Badan Ideal) value.

{:id="KCalCalculator.GetCorrection" class="method returns number"} <i/> GetCorrection()
: Method for GetCorrection

{:id="KCalCalculator.GetDietClass" class="method returns text"} <i/> GetDietClass(*kcal*{:.number})
: Get class of diet.

{:id="KCalCalculator.GetDietClassIndex" class="method returns number"} <i/> GetDietClassIndex(*kcal*{:.number})
: Method for GetDietClassIndex

{:id="KCalCalculator.GetKCal" class="method returns number"} <i/> GetKCal()
: Get KCal

{:id="KCalCalculator.GetKKB" class="method returns number"} <i/> GetKKB()
: Get KKB (Kebutuhan Kalori Basal) value.

{:id="KCalCalculator.GetMultiplier" class="method returns number"} <i/> GetMultiplier()
: Method for GetMultiplier

{:id="KCalCalculator.GetTimes" class="method returns number"} <i/> GetTimes()
: Method for GetTimes

## LabelUtils  {#LabelUtils}

Component for LabelUtils



### Properties  {#LabelUtils-Properties}

{:.properties}

{:id="LabelUtils.Language" .text} *Language*
: Set default language.

### Events  {#LabelUtils-Events}

{:.events}
None


### Methods  {#LabelUtils-Methods}

{:.methods}

{:id="LabelUtils.ChangeLanguage" class="method"} <i/> ChangeLanguage(*language*{:.text})
: To change language of all labels.

{:id="LabelUtils.DeleteComponent" class="method"} <i/> DeleteComponent(*label*{:.component})
: To delete component.

{:id="LabelUtils.ReplaceAll" class="method"} <i/> ReplaceAll(*label*{:.component},*multiLanguage*{:.dictionary})
: To change all of texts and languages with specific id.

{:id="LabelUtils.SetComponent" class="method"} <i/> SetComponent(*label*{:.component},*multiLanguage*{:.dictionary})
: To set components.

{:id="LabelUtils.Text" class="method"} <i/> Text(*label*{:.component},*text*{:.text},*language*{:.text})
: To change text with specific language and id.

## ListUtils  {#ListUtils}

Component for ListUtils



### Properties  {#ListUtils-Properties}

{:.properties}
None


### Events  {#ListUtils-Events}

{:.events}
None


### Methods  {#ListUtils-Methods}

{:.methods}

{:id="ListUtils.ReplaceAllItems" class="method returns list"} <i/> ReplaceAllItems(*list*{:.list},*segment*{:.text},*replacement*{:.text})
: To using replace all text function on all item in list.

## MarchUtils  {#MarchUtils}

Component for MarchUtils



### Properties  {#MarchUtils-Properties}

{:.properties}
None


### Events  {#MarchUtils-Events}

{:.events}

{:id="MarchUtils.GalleryRefreshed"} GalleryRefreshed(*filePath*{:.text},*uri*{:.text})
: An Event that occurrs when gallery has been refreshed.

### Methods  {#MarchUtils-Methods}

{:.methods}

{:id="MarchUtils.ConvertJSONString" class="method returns any"} <i/> ConvertJSONString(*json*{:.any})
: Returns a dictionary after converting data of onesignal.

{:id="MarchUtils.FormatDecimal" class="method returns any"} <i/> FormatDecimal(*format*{:.text},*value*{:.any})
: For format decimal places.

{:id="MarchUtils.GetHeight" class="method returns number"} <i/> GetHeight(*percent*{:.number},*componentHeight*{:.number})
: Get height of component from percent.

{:id="MarchUtils.Glucose" class="method returns any"} <i/> Glucose(*contrast*{:.number},*correlation*{:.number},*energy*{:.number},*homogeneity*{:.number})
: For calculate glucose.

{:id="MarchUtils.ListToDictionary" class="method returns dictionary"} <i/> ListToDictionary(*list*{:.list},*tags*{:.list})
: To convert list with value list to dictionary.

{:id="MarchUtils.MapFloat" class="method returns number"} <i/> MapFloat(*value*{:.number},*fromMin*{:.number},*fromMax*{:.number},*toMin*{:.number},*toMax*{:.number})
: For using map function.

{:id="MarchUtils.MapInteger" class="method returns number"} <i/> MapInteger(*value*{:.number},*fromMin*{:.number},*fromMax*{:.number},*toMin*{:.number},*toMax*{:.number})
: For using map function.

{:id="MarchUtils.RefreshGallery" class="method"} <i/> RefreshGallery(*filePath*{:.text})
: To Refresh gallery with specific file path.

{:id="MarchUtils.RemoveAllContainsValue" class="method returns list"} <i/> RemoveAllContainsValue(*list*{:.list},*keyPath*{:.text},*value*{:.any})
: Returns a list after remove all contains value in the given list with dictionary value.

{:id="MarchUtils.SortByDate" class="method returns list"} <i/> SortByDate(*list*{:.list},*keyPath*{:.text},*pattern*{:.text},*sortType*{:.com.marchtech.MarchUtils.helpers.SortEnum})
: Returns a list after sort the given list with dictionary value by date.

{:id="MarchUtils.SortByDigit" class="method returns list"} <i/> SortByDigit(*list*{:.list},*keyPath*{:.text})
: Returns a list after sort the given list with dictionary value by digit.

{:id="MarchUtils.SortByLetter" class="method returns list"} <i/> SortByLetter(*list*{:.list},*keyPath*{:.text})
: Returns a list after sort the given list with dictionary value by letter.

## OTPSender  {#OTPSender}

Component for OTPSender



### Properties  {#OTPSender-Properties}

{:.properties}

{:id="OTPSender.URL" .text} *URL*
: The URL for the web request.

### Events  {#OTPSender-Events}

{:.events}

{:id="OTPSender.onFailed"} onFailed()
: Occurs when otp has not sended.

{:id="OTPSender.onSuccess"} onSuccess()
: Occurs when otp has been sended.

{:id="OTPSender.onTimeout"} onTimeout()
: Event for onTimeout

### Methods  {#OTPSender-Methods}

{:.methods}

{:id="OTPSender.Send" class="method"} <i/> Send(*to*{:.text},*subject*{:.text},*otp*{:.text},*msg*{:.text})
: For sending OTP to email.

## OTPUtils  {#OTPUtils}

Component for OTPUtils



### Properties  {#OTPUtils-Properties}

{:.properties}

{:id="OTPUtils.Second" .number} *Second*
: seconds.

### Events  {#OTPUtils-Events}

{:.events}
None


### Methods  {#OTPUtils-Methods}

{:.methods}

{:id="OTPUtils.Initialize" class="method"} <i/> Initialize(*label*{:.component},*layout1*{:.component},*layout2*{:.component})
: To initialize components. note: layout = Horizontal Arrangement, layout2 = Vertical Arrangement.

{:id="OTPUtils.ReCount" class="method"} <i/> ReCount()
: To re-count second.

{:id="OTPUtils.Start" class="method"} <i/> Start()
: To start utils.

## OneSignalScheduler  {#OneSignalScheduler}

Component for OneSignalScheduler



### Properties  {#OneSignalScheduler-Properties}

{:.properties}

{:id="OneSignalScheduler.AppId" .text} *AppId*
: To set app id of onesignal.

{:id="OneSignalScheduler.Priority" .number} *Priority*
: To set priority of notification, value: 0 - 10.

{:id="OneSignalScheduler.RestAPIKey" .text} *RestAPIKey*
: To set rest api key of onesignal.

### Events  {#OneSignalScheduler-Events}

{:.events}

{:id="OneSignalScheduler.Test"} Test(*value*{:.text})
: Event for Test

{:id="OneSignalScheduler.onCreated"} onCreated(*id*{:.text},*notificationId*{:.text})
: Occurs when notification has been scheduled.

{:id="OneSignalScheduler.onDeleted"} onDeleted(*id*{:.text})
: Occurs when notification has been deleted.

{:id="OneSignalScheduler.onFailed"} onFailed(*response*{:.text},*id*{:.text},*messages*{:.text})
: Occurs when notification can't scheduled/deleted.

{:id="OneSignalScheduler.onTimedOut"} onTimedOut(*response*{:.text},*id*{:.text})
: Occurs when has timedout.

### Methods  {#OneSignalScheduler-Methods}

{:.methods}

{:id="OneSignalScheduler.CreateSchedule" class="method"} <i/> CreateSchedule(*id*{:.text},*segment*{:.text},*messages*{:.dictionary},*title*{:.dictionary},*dateTime*{:.text},*timezone*{:.text},*data*{:.dictionary})
: To create schedule of onesignal push notifications.

{:id="OneSignalScheduler.CreateScheduleToIds" class="method"} <i/> CreateScheduleToIds(*id*{:.text},*subscriptionIds*{:.list},*messages*{:.dictionary},*title*{:.dictionary},*dateTime*{:.text},*timezone*{:.text},*data*{:.dictionary})
: To create schedule of onesignal push notifications with player ids.

{:id="OneSignalScheduler.DeleteSchedule" class="method"} <i/> DeleteSchedule(*id*{:.text},*notificationId*{:.text})
: To delete schedule of onesignal push notifications.

## QRGenerator  {#QRGenerator}

Component for QRGenerator



### Properties  {#QRGenerator-Properties}

{:.properties}

{:id="QRGenerator.BackgroundColor" .color} *BackgroundColor*
: Set background color of qr code.

{:id="QRGenerator.Height" .number} *Height*
: Set height of qr code.

{:id="QRGenerator.QRColor" .color} *QRColor*
: Set color of qr code.

{:id="QRGenerator.UseAdditionalDecoders" .boolean .wo} *UseAdditionalDecoders*
: Specifies whether decoders should use additional hints.

{:id="QRGenerator.Width" .number} *Width*
: Set width of qr code.

### Events  {#QRGenerator-Events}

{:.events}

{:id="QRGenerator.Decoded"} Decoded(*barFormat*{:.com.marchtech.QRGenerator.helpers.BarFormatEnum},*result*{:.text})
: An event that occurrs when barcode has been decoded.

{:id="QRGenerator.ErrorOccurred"} ErrorOccurred(*functionName*{:.text},*messages*{:.text})
: An event that occurrs when generated/decoded barcode failed.

{:id="QRGenerator.Generated"} Generated(*filePath*{:.text})
: An event that occurrs when barcode has been genarated.

{:id="QRGenerator.GeneratedOnImage"} GeneratedOnImage()
: An event that occurrs when barcode has been genarated on image component.

### Methods  {#QRGenerator-Methods}

{:.methods}

{:id="QRGenerator.Decode" class="method"} <i/> Decode(*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*fileName*{:.text})
: To decode barcode from file.

{:id="QRGenerator.Generate" class="method"} <i/> Generate(*content*{:.text},*scope*{:.com.google.appinventor.components.common.FileScopeEnum},*fileName*{:.text},*logoScope*{:.com.google.appinventor.components.common.FileScopeEnum},*logoPath*{:.text},*fileFormat*{:.com.marchtech.QRGenerator.helpers.FileFormatEnum},*barFormat*{:.com.marchtech.QRGenerator.helpers.BarFormatEnum},*charset*{:.text})
: To generate qr code.

{:id="QRGenerator.GenerateOnImage" class="method"} <i/> GenerateOnImage(*imageComponent*{:.component},*content*{:.text},*logoScope*{:.com.google.appinventor.components.common.FileScopeEnum},*logoPath*{:.text},*barFormat*{:.com.marchtech.QRGenerator.helpers.BarFormatEnum},*charset*{:.text})
: To generate qr code on image component without save it.

## SimpleBluetooth  {#SimpleBluetooth}

Component for SimpleBluetooth



### Properties  {#SimpleBluetooth-Properties}

{:.properties}

{:id="SimpleBluetooth.AddressesAndNames" .list .ro .bo} *AddressesAndNames*
: The addresses and names of paired Bluetooth devices

{:id="SimpleBluetooth.Available" .boolean .ro .bo} *Available*
: Returns `true`{:.logic.block} if Bluetooth is available on the device,
 `false`{:.logic.block} otherwise.

{:id="SimpleBluetooth.CharacterEncoding" .text} *CharacterEncoding*
: Returns the character encoding to use when sending and receiving text.

{:id="SimpleBluetooth.DelimiterByte" .number} *DelimiterByte*
: Returns the delimiter byte to use when passing a negative number for the
 numberOfBytes parameter when calling ReceiveText, ReceiveSignedBytes, or
 ReceiveUnsignedBytes.

{:id="SimpleBluetooth.Enabled" .boolean .ro .bo} *Enabled*
: Returns `true`{:.logic.block} if Bluetooth is enabled, `false`{:.logic.block} otherwise.

{:id="SimpleBluetooth.HighByteFirst" .boolean} *HighByteFirst*
: Specifies whether numbers are sent and received with the most significant
 byte first.

{:id="SimpleBluetooth.Interval" .number} *Interval*
: Property for Interval

{:id="SimpleBluetooth.IsConnected" .boolean .ro .bo} *IsConnected*
: Returns `frue`{:.logic.block} if a connection to a Bluetooth device has been made.

{:id="SimpleBluetooth.Secure" .boolean} *Secure*
: Specifies whether a secure connection should be used.

{:id="SimpleBluetooth.SignedBytes" .number} *SignedBytes*
: Property for SignedBytes

{:id="SimpleBluetooth.TextBytes" .number} *TextBytes*
: Property for TextBytes

{:id="SimpleBluetooth.UnsignedBytes" .number} *UnsignedBytes*
: Property for UnsignedBytes

### Events  {#SimpleBluetooth-Events}

{:.events}

{:id="SimpleBluetooth.GotRaw"} GotRaw(*result*{:.list},*totalBytes*{:.number})
: The incoming data event.

{:id="SimpleBluetooth.GotSigned"} GotSigned(*result*{:.list})
: Event for GotSigned

{:id="SimpleBluetooth.GotText"} GotText(*result*{:.text})
: The incoming data event.

{:id="SimpleBluetooth.GotUnsigned"} GotUnsigned(*result*{:.list})
: Event for GotUnsigned

### Methods  {#SimpleBluetooth-Methods}

{:.methods}

{:id="SimpleBluetooth.BytesAvailableToReceive" class="method returns number"} <i/> BytesAvailableToReceive()
: Returns number of bytes available from the input stream.

{:id="SimpleBluetooth.Connect" class="method returns boolean"} <i/> Connect(*address*{:.text})
: Connect to the Bluetooth device with the specified address and the Serial Port Profile (SPP). Returns true if the connection was successful.

{:id="SimpleBluetooth.ConnectWithUUID" class="method returns boolean"} <i/> ConnectWithUUID(*address*{:.text},*uuid*{:.text})
: Connect to the Bluetooth device with the specified address and UUID. Returns true if the connection was successful.

{:id="SimpleBluetooth.Disconnect" class="method"} <i/> Disconnect()
: Disconnects from the connected Bluetooth device.

{:id="SimpleBluetooth.IsDevicePaired" class="method returns boolean"} <i/> IsDevicePaired(*address*{:.text})
: Checks whether the Bluetooth device with the specified address is paired.

{:id="SimpleBluetooth.ReceiveSigned1ByteNumber" class="method returns number"} <i/> ReceiveSigned1ByteNumber()
: Reads a signed 1-byte number.

{:id="SimpleBluetooth.ReceiveSigned2ByteNumber" class="method returns number"} <i/> ReceiveSigned2ByteNumber()
: Reads a signed 2-byte number.

{:id="SimpleBluetooth.ReceiveSigned4ByteNumber" class="method returns number"} <i/> ReceiveSigned4ByteNumber()
: Reads a signed 4-byte number.

{:id="SimpleBluetooth.ReceiveSignedBytes" class="method returns list"} <i/> ReceiveSignedBytes(*numberOfBytes*{:.number})
: Reads a number of signed bytes from the input stream and returns them as
 a List.

   If numberOfBytes is negative, this method reads until a delimiter byte
 value is read. The delimiter byte value is included in the returned list.

{:id="SimpleBluetooth.ReceiveText" class="method returns text"} <i/> ReceiveText(*numberOfBytes*{:.number})
: Reads a number of bytes from the input stream and converts them to text.

   If numberOfBytes is negative, read until a delimiter byte value is read.

{:id="SimpleBluetooth.ReceiveUnsigned1ByteNumber" class="method returns number"} <i/> ReceiveUnsigned1ByteNumber()
: Reads an unsigned 1-byte number.

{:id="SimpleBluetooth.ReceiveUnsigned2ByteNumber" class="method returns number"} <i/> ReceiveUnsigned2ByteNumber()
: Reads an unsigned 2-byte number.

{:id="SimpleBluetooth.ReceiveUnsigned4ByteNumber" class="method returns number"} <i/> ReceiveUnsigned4ByteNumber()
: Reads an unsigned 4-byte number.

{:id="SimpleBluetooth.ReceiveUnsignedBytes" class="method returns list"} <i/> ReceiveUnsignedBytes(*numberOfBytes*{:.number})
: Reads a number of unsigned bytes from the input stream and returns them as
 a List.

   If numberOfBytes is negative, this method reads until a delimiter byte
 value is read. The delimiter byte value is included in the returned list.

{:id="SimpleBluetooth.Send1ByteNumber" class="method"} <i/> Send1ByteNumber(*number*{:.text})
: Decodes the given number String to an integer and writes it as one byte
 to the output stream.

   If the number could not be decoded to an integer, or the integer would not
 fit in one byte, then the Form's ErrorOccurred event is triggered and this
 method returns without writing any bytes to the output stream.

{:id="SimpleBluetooth.Send2ByteNumber" class="method"} <i/> Send2ByteNumber(*number*{:.text})
: Decodes the given number String to an integer and writes it as two bytes
 to the output stream.

   If the number could not be decoded to an integer, or the integer would not
 fit in two bytes, then the Form's ErrorOccurred event is triggered and this
 method returns without writing any bytes to the output stream.

{:id="SimpleBluetooth.Send4ByteNumber" class="method"} <i/> Send4ByteNumber(*number*{:.text})
: Decodes the given number String to an integer and writes it as four bytes
 to the output stream.

   If the number could not be decoded to an integer, or the integer would not
 fit in four bytes, then the Form's ErrorOccurred event is triggered and this
 method returns without writing any bytes to the output stream.

{:id="SimpleBluetooth.SendBytes" class="method"} <i/> SendBytes(*list*{:.list})
: Takes each element from the given list, converts it to a String, decodes
 the String to an integer, and writes it as one byte to the output stream.

   If an element could not be decoded to an integer, or the integer would not
 fit in one byte, then the Form's ErrorOccurred event is triggered and this
 method returns without writing any bytes to the output stream.

{:id="SimpleBluetooth.SendText" class="method"} <i/> SendText(*text*{:.text})
: Converts the given text to bytes and writes them to the output stream.

{:id="SimpleBluetooth.StartGetRaw" class="method"} <i/> StartGetRaw(*delay*{:.number})
: To start getting raw data.

{:id="SimpleBluetooth.StartGetSigned" class="method"} <i/> StartGetSigned(*delay*{:.number})
: To start getting data.

{:id="SimpleBluetooth.StartGetText" class="method"} <i/> StartGetText(*delay*{:.number})
: To start getting data.

{:id="SimpleBluetooth.StartGetUnsigned" class="method"} <i/> StartGetUnsigned(*delay*{:.number})
: To start getting data.

{:id="SimpleBluetooth.StopGetAll" class="method"} <i/> StopGetAll()
: To stop getting all data.

{:id="SimpleBluetooth.StopGetRaw" class="method"} <i/> StopGetRaw()
: To stop getting data.

{:id="SimpleBluetooth.StopGetSigned" class="method"} <i/> StopGetSigned()
: To stop getting data.

{:id="SimpleBluetooth.StopGetText" class="method"} <i/> StopGetText()
: To stop getting data.

{:id="SimpleBluetooth.StopGetUnsigned" class="method"} <i/> StopGetUnsigned()
: To stop getting data.

{:id="SimpleBluetooth.ToSignedBytes" class="method returns list"} <i/> ToSignedBytes(*numberOfBytes*{:.number})
: Converting raw data to signed bytes

{:id="SimpleBluetooth.ToString" class="method returns text"} <i/> ToString(*numberOfBytes*{:.number})
: Converting raw data to string

{:id="SimpleBluetooth.ToUnsignedBytes" class="method returns list"} <i/> ToUnsignedBytes(*numberOfBytes*{:.number})
: Converting raw data to unsigned bytes

## SimpleButton  {#SimpleButton}

Component for SimpleButton



### Properties  {#SimpleButton-Properties}

{:.properties}

{:id="SimpleButton.BackgroundColor" .color} *BackgroundColor*
: Specifies the `SimpleButton`'s background color as an alpha-red-green-blue
 integer.  If an [`Image`](#SimpleButton.Image) has been set, the color
 change will not be visible until the [`Image`](#SimpleButton.Image) is removed.

{:id="SimpleButton.Enabled" .boolean} *Enabled*
: Specifies whether the `SimpleButton` should be active and clickable.

{:id="SimpleButton.FontBold" .boolean} *FontBold*
: Specifies whether the text of the `SimpleButton` should be bold.
 Some fonts do not support bold.

{:id="SimpleButton.FontItalic" .boolean} *FontItalic*
: Specifies whether the text of the `SimpleButton` should be italic.
 Some fonts do not support italic.

{:id="SimpleButton.FontSize" .number} *FontSize*
: Specifies the text font size of the `SimpleButton`, measured in sp(scale-independent pixels).

{:id="SimpleButton.FontTypeface" .text .do} *FontTypeface*
: Specifies the text font face of the `SimpleButton` as default, serif, sans
 serif, monospace, or custom font typeface. To add a custom typeface,
 upload a .ttf file to the project's media.

{:id="SimpleButton.Height" .number .bo} *Height*
: Specifies the `SimpleButton`'s vertical height, measured in pixels.

{:id="SimpleButton.HeightPercent" .number .wo .bo} *HeightPercent*
: Specifies the `SimpleButton`'s vertical height as a percentage
 of the [`Screen`'s `Height`](userinterface.html#Screen.Height).

{:id="SimpleButton.Image" .text} *Image*
: Specifies the path of the `SimpleButton`'s image. If there is both an `Image` and a
 [`BackgroundColor`](#SimpleButton.BackgroundColor) specified, only the `Image` will be visible.

{:id="SimpleButton.Shape" .number .do} *Shape*
: Specifies the shape of the `SimpleButton`. The valid values for this property are `0` (default),
 `1` (rounded), `2` (rectangle), and `3` (oval). The `Shape` will not be visible if an
 [`Image`](#SimpleButton.Image) is used.

{:id="SimpleButton.ShowFeedback" .boolean} *ShowFeedback*
: Specifies if a visual feedback should be shown when a `SimpleButton` with an assigned
 [`Image`](#SimpleButton.Image) is pressed.

{:id="SimpleButton.Text" .text} *Text*
: Specifies the text displayed by the `SimpleButton`.

{:id="SimpleButton.TextAlignment" .number .do} *TextAlignment*
: Specifies the alignment of the `SimpleButton`'s text. Valid values are:
 `0` (normal; e.g., left-justified if text is written left to right),
 `1` (center), or
 `2` (opposite; e.g., right-justified if text is written left to right).

{:id="SimpleButton.TextColor" .color} *TextColor*
: Specifies the text color of the `SimpleButton` as an alpha-red-green-blue
 integer.

{:id="SimpleButton.Visible" .boolean} *Visible*
: Specifies whether the `SimpleButton` should be visible on the screen.  Value is `true`{:.logic.block}
 if the `SimpleButton` is showing and `false`{:.logic.block} if hidden.

{:id="SimpleButton.Width" .number .bo} *Width*
: Specifies the horizontal width of the `SimpleButton`, measured in pixels.

{:id="SimpleButton.WidthPercent" .number .wo .bo} *WidthPercent*
: Specifies the horizontal width of the `SimpleButton` as a percentage
 of the [`Screen`'s `Width`](userinterface.html#Screen.Width).

### Events  {#SimpleButton-Events}

{:.events}

{:id="SimpleButton.Click"} Click()
: User tapped and released the button.

{:id="SimpleButton.GotFocus"} GotFocus()
: Indicates the cursor moved over the `SimpleButton` so it is now possible
 to click it.

{:id="SimpleButton.LongClick"} LongClick()
: User held the button down.

{:id="SimpleButton.LostFocus"} LostFocus()
: Indicates the cursor moved away from the `SimpleButton` so it is now no
 longer possible to click it.

{:id="SimpleButton.TouchDown"} TouchDown()
: Indicates that the `SimpleButton` was pressed down.

{:id="SimpleButton.TouchUp"} TouchUp()
: Indicates that the `SimpleButton` has been released.

### Methods  {#SimpleButton-Methods}

{:.methods}
None


## SimpleDateAndTime  {#SimpleDateAndTime}

Component for SimpleDateAndTime



### Properties  {#SimpleDateAndTime-Properties}

{:.properties}
None


### Events  {#SimpleDateAndTime-Events}

{:.events}

{:id="SimpleDateAndTime.ErrorOccurred"} ErrorOccurred(*message*{:.text})
: Event for ErrorOccurred

### Methods  {#SimpleDateAndTime-Methods}

{:.methods}

{:id="SimpleDateAndTime.Date" class="method returns text"} <i/> Date(*instant*{:.InstantInTime},*pattern*{:.text})
: Text representing the date of an instant in the specified pattern.

{:id="SimpleDateAndTime.DateAndTime" class="method returns text"} <i/> DateAndTime(*instant*{:.InstantInTime},*pattern*{:.text})
: Returns text representing the date and time of an instant in the specified pattern.

{:id="SimpleDateAndTime.GetDateAndTime" class="method returns InstantInTime"} <i/> GetDateAndTime(*string*{:.text},*pattern*{:.text})
: To get date and time in string with specific pattern.

{:id="SimpleDateAndTime.GetNow" class="method returns number"} <i/> GetNow(*dateTime*{:.text})
: Return hour, minute or second.

{:id="SimpleDateAndTime.GetToday" class="method returns text"} <i/> GetToday(*pattern*{:.text})
: To get date of today.

{:id="SimpleDateAndTime.GetTomorrow" class="method returns text"} <i/> GetTomorrow(*pattern*{:.text})
: To get date of tomorrow.

{:id="SimpleDateAndTime.GetYesterday" class="method returns text"} <i/> GetYesterday(*pattern*{:.text})
: To get date of yesterday.

{:id="SimpleDateAndTime.MakeInstantFromDate" class="method returns InstantInTime"} <i/> MakeInstantFromDate(*instant*{:.InstantInTime},*timeIfToday*{:.InstantInTime},*timeIfTomorrow*{:.InstantInTime})
: Method for MakeInstantFromDate

{:id="SimpleDateAndTime.MakeInstantFromStringTime" class="method returns InstantInTime"} <i/> MakeInstantFromStringTime(*time*{:.text},*separator*{:.text},*checkToday*{:.boolean})
: Method for MakeInstantFromStringTime

{:id="SimpleDateAndTime.MakeInstantFromTime" class="method returns InstantInTime"} <i/> MakeInstantFromTime(*hour*{:.number},*minute*{:.number},*second*{:.number},*checkToday*{:.boolean})
: Method for MakeInstantFromTime

{:id="SimpleDateAndTime.RecentDate" class="method returns InstantInTime"} <i/> RecentDate(*date1*{:.text},*date2*{:.text},*pattern*{:.text})
: Return the most recent of two dates.

{:id="SimpleDateAndTime.ReduceHours" class="method returns InstantInTime"} <i/> ReduceHours(*instant*{:.InstantInTime},*quantity*{:.number})
: Returns an instant in time some hour before the given instant.

{:id="SimpleDateAndTime.ReduceMinutes" class="method returns InstantInTime"} <i/> ReduceMinutes(*instant*{:.InstantInTime},*quantity*{:.number})
: Returns an instant in time some minutes before the given instant.

{:id="SimpleDateAndTime.ReduceSeconds" class="method returns InstantInTime"} <i/> ReduceSeconds(*instant*{:.InstantInTime},*quantity*{:.number})
: Returns an instant in time some seconds before the given instant.

{:id="SimpleDateAndTime.SetTime" class="method returns InstantInTime"} <i/> SetTime(*instant*{:.InstantInTime},*hour*{:.number},*minute*{:.number},*second*{:.number})
: Returns an instant with specific time in the given instant.

{:id="SimpleDateAndTime.SplitTime" class="method returns number"} <i/> SplitTime(*time*{:.text},*string*{:.text},*separator*{:.text})
: Return splitted time from string.

{:id="SimpleDateAndTime.TimeName" class="method returns text"} <i/> TimeName(*instant*{:.InstantInTime})
: Return name of time "MORNING", "DAY", "AFTERNOON", "EVENING" or "NIGHT".

{:id="SimpleDateAndTime.Today" class="method returns InstantInTime"} <i/> Today()
: To get instant of today.

{:id="SimpleDateAndTime.TodayWithStringTime" class="method returns InstantInTime"} <i/> TodayWithStringTime(*time*{:.text},*separator*{:.text})
: To get instant of today.

{:id="SimpleDateAndTime.TodayWithTime" class="method returns InstantInTime"} <i/> TodayWithTime(*hour*{:.number},*minute*{:.number},*second*{:.number})
: To get instant of today.

{:id="SimpleDateAndTime.Tomorrow" class="method returns InstantInTime"} <i/> Tomorrow()
: To get instant of tomorrow.

{:id="SimpleDateAndTime.TomorrowWithStringTime" class="method returns InstantInTime"} <i/> TomorrowWithStringTime(*time*{:.text},*separator*{:.text})
: To get instant of tomorrow.

{:id="SimpleDateAndTime.TomorrowWithTime" class="method returns InstantInTime"} <i/> TomorrowWithTime(*hour*{:.number},*minute*{:.number},*second*{:.number})
: To get instant of tomorrow.

{:id="SimpleDateAndTime.Yesterday" class="method returns InstantInTime"} <i/> Yesterday()
: To get instant of yesterday.

{:id="SimpleDateAndTime.YesterdayWithStringTime" class="method returns InstantInTime"} <i/> YesterdayWithStringTime(*time*{:.text},*separator*{:.text})
: To get instant of yesterday.

{:id="SimpleDateAndTime.YesterdayWithTime" class="method returns InstantInTime"} <i/> YesterdayWithTime(*hour*{:.number},*minute*{:.number},*second*{:.number})
: To get instant of yesterday.

{:id="SimpleDateAndTime.isToday" class="method returns boolean"} <i/> isToday(*instant*{:.InstantInTime})
: Return true or false if is today.

{:id="SimpleDateAndTime.isYesterday" class="method returns boolean"} <i/> isYesterday(*instant*{:.InstantInTime})
: Return true or false if is yesterday.

## SimpleFirebase  {#SimpleFirebase}

Component for SimpleFirebase



### Properties  {#SimpleFirebase-Properties}

{:.properties}
None


### Events  {#SimpleFirebase-Events}

{:.events}
None


### Methods  {#SimpleFirebase-Methods}

{:.methods}
None


## SimpleMenu  {#SimpleMenu}

Component for SimpleMenu



### Properties  {#SimpleMenu-Properties}

{:.properties}
None


### Events  {#SimpleMenu-Events}

{:.events}

{:id="SimpleMenu.Test"} Test(*value1*{:.any},*value2*{:.any})
: Event for Test

### Methods  {#SimpleMenu-Methods}

{:.methods}

{:id="SimpleMenu.Add" class="method"} <i/> Add(*component*{:.component},*placement*{:.com.marchtech.SimpleMenu.helpers.PlacementEnum})
: To add view on parent layout.

{:id="SimpleMenu.Initialize" class="method"} <i/> Initialize(*layout*{:.component})
: To initialize component.

## SimplePedometer  {#SimplePedometer}

Component for SimplePedometer



### Properties  {#SimplePedometer-Properties}

{:.properties}

{:id="SimplePedometer.ElapsedTime" .number .ro .bo} *ElapsedTime*
: Time elapsed in milliseconds since the SimplePedometer was started

{:id="SimplePedometer.ResetDaily" .boolean} *ResetDaily*
: To reset daily of detected steps

{:id="SimplePedometer.StepMode" .text} *StepMode*
: The mode of step detector.

{:id="SimplePedometer.StopDetectionTimeout" .number} *StopDetectionTimeout*
: The duration in milliseconds of idleness (no steps detected) after which to go into a "stopped" state

{:id="SimplePedometer.StrideLength" .number} *StrideLength*
: Set the average stride length in centimeters

### Events  {#SimplePedometer-Events}

{:.events}

{:id="SimplePedometer.StepsDetected"} StepsDetected()
: This event is run when a raw step is detected

### Methods  {#SimplePedometer-Methods}

{:.methods}

{:id="SimplePedometer.GetDistances" class="method returns number"} <i/> GetDistances(*measure*{:.text})
: The approximate distance traveled.

{:id="SimplePedometer.GetRawData" class="method returns list"} <i/> GetRawData()
: To get saved data.

{:id="SimplePedometer.GetSteps" class="method returns number"} <i/> GetSteps()
: The number of simple steps taken since the SimplePedometer has started.

{:id="SimplePedometer.Initialize" class="method"} <i/> Initialize(*data*{:.list})
: To initialize counting steps.

{:id="SimplePedometer.Reset" class="method"} <i/> Reset()
: Resets the step counter, distance measure and time running

{:id="SimplePedometer.Save" class="method"} <i/> Save()
: Saves the pedometer state to the phone

{:id="SimplePedometer.Start" class="method"} <i/> Start()
: Start counting steps

{:id="SimplePedometer.Stop" class="method"} <i/> Stop()
: Stop counting steps

## SimplePedometer_v1  {#SimplePedometer_v1}

Component for SimplePedometer_v1



### Properties  {#SimplePedometer_v1-Properties}

{:.properties}
None


### Events  {#SimplePedometer_v1-Events}

{:.events}

{:id="SimplePedometer_v1.walkSteps"} walkSteps(*steps*{:.number})
: An event that occurs when steps changed

### Methods  {#SimplePedometer_v1-Methods}

{:.methods}
None


## SimplePedometer_v2  {#SimplePedometer_v2}

Component for SimplePedometer_v2



### Properties  {#SimplePedometer_v2-Properties}

{:.properties}
None


### Events  {#SimplePedometer_v2-Events}

{:.events}

{:id="SimplePedometer_v2.SimpleStep"} SimpleStep(*steps*{:.number},*distance*{:.number})
: This event is run when a raw step is detected

{:id="SimplePedometer_v2.WalkStep"} WalkStep(*steps*{:.number},*distance*{:.number})
: This event is run when a walking step is detected

### Methods  {#SimplePedometer_v2-Methods}

{:.methods}

{:id="SimplePedometer_v2.Start" class="method"} <i/> Start()
: To start testing

## SimplePedometer_v3  {#SimplePedometer_v3}

Component for SimplePedometer_v3



### Properties  {#SimplePedometer_v3-Properties}

{:.properties}
None


### Events  {#SimplePedometer_v3-Events}

{:.events}

{:id="SimplePedometer_v3.SimpleStep"} SimpleStep(*steps*{:.number},*distance*{:.number})
: This event is run when a raw step is detected

{:id="SimplePedometer_v3.WalkStep"} WalkStep(*steps*{:.number},*distance*{:.number})
: This event is run when a walking step is detected

### Methods  {#SimplePedometer_v3-Methods}

{:.methods}

{:id="SimplePedometer_v3.Start" class="method"} <i/> Start()
: Start counting steps

{:id="SimplePedometer_v3.Stop" class="method"} <i/> Stop()
: Stop counting steps

## SimpleSpreadsheet  {#SimpleSpreadsheet}

Component for SimpleSpreadsheet



### Properties  {#SimpleSpreadsheet-Properties}

{:.properties}

{:id="SimpleSpreadsheet.ID" .text} *ID*
: The id of spreadsheet.

{:id="SimpleSpreadsheet.SheetID" .text} *SheetID*
: The sheet id of spreedsheet.

{:id="SimpleSpreadsheet.SheetName" .text} *SheetName*
: To set active sheet.

{:id="SimpleSpreadsheet.URL" .text} *URL*
: The URL of google scripts.

### Events  {#SimpleSpreadsheet-Events}

{:.events}

{:id="SimpleSpreadsheet.Added"} Added(*value*{:.any})
: On added new value.

{:id="SimpleSpreadsheet.AllDeleted"} AllDeleted()
: On all deleted.

{:id="SimpleSpreadsheet.AllEdited"} AllEdited(*tag*{:.any},*value*{:.any})
: On edited all value.

{:id="SimpleSpreadsheet.DataChanged"} DataChanged(*tag*{:.any},*value*{:.any})
: Occurrs when data has been changed.

{:id="SimpleSpreadsheet.Deleted"} Deleted(*tag*{:.any})
: On deleted tag.

{:id="SimpleSpreadsheet.Edited"} Edited(*tag*{:.any},*value*{:.any})
: On edited value.

{:id="SimpleSpreadsheet.GotAll"} GotAll(*as*{:.com.marchtech.SimpleSpreadsheet.helpers.AsEnum},*value*{:.any})
: On got all value.

{:id="SimpleSpreadsheet.GotAllValue"} GotAllValue(*key*{:.any},*value*{:.any})
: On got all value with specific tag.

{:id="SimpleSpreadsheet.GotValue"} GotValue(*as*{:.com.marchtech.SimpleSpreadsheet.helpers.AsEnum},*tag*{:.any},*value*{:.any})
: On got value with specific tag.

{:id="SimpleSpreadsheet.MultipleEdited"} MultipleEdited(*tags*{:.any},*values*{:.any})
: On edited multiple value.

{:id="SimpleSpreadsheet.OnFailed"} OnFailed(*functionName*{:.text},*messages*{:.text})
: Occurrs when failed add, edit or delete

### Methods  {#SimpleSpreadsheet-Methods}

{:.methods}

{:id="SimpleSpreadsheet.Add" class="method"} <i/> Add(*value*{:.any})
: To add new value with list or dictionary.

{:id="SimpleSpreadsheet.Delete" class="method"} <i/> Delete(*tag*{:.text})
: To delete specific tag.

{:id="SimpleSpreadsheet.DeleteAll" class="method"} <i/> DeleteAll()
: To delete all.

{:id="SimpleSpreadsheet.Edit" class="method"} <i/> Edit(*tag*{:.text},*value*{:.any})
: To edit value with specific tag.

{:id="SimpleSpreadsheet.EditAll" class="method"} <i/> EditAll(*value*{:.any})
: To edit all value with specific tag.

{:id="SimpleSpreadsheet.EditMultiple" class="method"} <i/> EditMultiple(*tags*{:.list},*values*{:.list})
: To edit multiple value with specific tags as list.

{:id="SimpleSpreadsheet.Get" class="method"} <i/> Get(*as*{:.com.marchtech.SimpleSpreadsheet.helpers.AsEnum},*tag*{:.text},*valueIfTagNotThere*{:.any})
: To get value with specific tag.

{:id="SimpleSpreadsheet.GetAll" class="method"} <i/> GetAll(*as*{:.com.marchtech.SimpleSpreadsheet.helpers.AsEnum})
: To get all value.

{:id="SimpleSpreadsheet.GetAllKeys" class="method returns list"} <i/> GetAllKeys()
: To get all keys after initialized.

{:id="SimpleSpreadsheet.GetAllValue" class="method"} <i/> GetAllValue(*key*{:.text})
: To get all value with specific tag.

{:id="SimpleSpreadsheet.Initialize" class="method"} <i/> Initialize(*keys*{:.list})
: To initialize extension.

## StringFunctions  {#StringFunctions}

Component for StringFunctions



### Properties  {#StringFunctions-Properties}

{:.properties}
None


### Events  {#StringFunctions-Events}

{:.events}
None


### Methods  {#StringFunctions-Methods}

{:.methods}

{:id="StringFunctions.ChangeCase" class="method returns text"} <i/> ChangeCase(*string*{:.text},*toCase*{:.text})
: Return the changed case with the given parameters.

{:id="StringFunctions.CountCharacter" class="method returns number"} <i/> CountCharacter(*string*{:.text},*character*{:.text},*ignoreCapitalization*{:.boolean})
: Return the amount of the specific character appear.

{:id="StringFunctions.FirebaseValueToList" class="method returns list"} <i/> FirebaseValueToList(*value*{:.text})
: Return list from firebase value in GotValue function.

{:id="StringFunctions.GetFirebaseValue" class="method returns text"} <i/> GetFirebaseValue(*value*{:.text},*segment*{:.text},*replacement*{:.text})
: Return value from firebase in GotValue function.

{:id="StringFunctions.IndexAllOfSpecificCharacter" class="method returns list"} <i/> IndexAllOfSpecificCharacter(*string*{:.text},*character*{:.text},*ignoreCapitalization*{:.boolean})
: Return all of index with specific character.

{:id="StringFunctions.IndexFromList" class="method returns number"} <i/> IndexFromList(*string*{:.text},*list*{:.list})
: Return the index of string from the list.

{:id="StringFunctions.IndexFromListString" class="method returns number"} <i/> IndexFromListString(*string*{:.text},*listString*{:.text})
: Return the index of string from the list (from string).

{:id="StringFunctions.IndexOfSpecificCharacter" class="method returns number"} <i/> IndexOfSpecificCharacter(*string*{:.text},*character*{:.text},*ignoreCapitalization*{:.boolean})
: Return index of first specific character. return -1 if null

{:id="StringFunctions.RandomString" class="method returns text"} <i/> RandomString(*mode*{:.text},*length*{:.number})
: Return random string from specific mode.

{:id="StringFunctions.StringFromIndexOfList" class="method returns text"} <i/> StringFromIndexOfList(*index*{:.number},*list*{:.list})
: Return the string of index from the list.

{:id="StringFunctions.StringFromIndexOfListString" class="method returns text"} <i/> StringFromIndexOfListString(*index*{:.number},*listString*{:.text})
: Return the string of index from the list (from string).

{:id="StringFunctions.StringToDictionary" class="method returns dictionary"} <i/> StringToDictionary(*string*{:.text})
: Return dictionary from string (json).

{:id="StringFunctions.TwoDigit" class="method returns text"} <i/> TwoDigit(*value*{:.number})
: Return integer 1 digit to 2 digits. e.g "1" to "01".

{:id="StringFunctions.isContainsNumber" class="method returns boolean"} <i/> isContainsNumber(*string*{:.text})
: Return true or false if string contains number.

{:id="StringFunctions.isContainsSpecificCharacter" class="method returns boolean"} <i/> isContainsSpecificCharacter(*string*{:.text},*character*{:.text})
: Return true or false if string contains specific character.

{:id="StringFunctions.isContainsSymbol" class="method returns boolean"} <i/> isContainsSymbol(*string*{:.text})
: Return true or false if string contains symbol.

{:id="StringFunctions.isDigit" class="method returns boolean"} <i/> isDigit(*string*{:.text})
: Return true or false if string is a digit.

{:id="StringFunctions.isEndsWith" class="method returns boolean"} <i/> isEndsWith(*string*{:.text},*character*{:.text},*ignoreCapitalization*{:.boolean})
: Return true or false if string ends with specific character.

{:id="StringFunctions.isLetter" class="method returns boolean"} <i/> isLetter(*string*{:.text})
: Return true or false if string is a letter.

{:id="StringFunctions.isLetterOrDigit" class="method returns boolean"} <i/> isLetterOrDigit(*string*{:.text})
: Return true or false if string is a letter or digit.

{:id="StringFunctions.isName" class="method returns boolean"} <i/> isName(*string*{:.text})
: Return true or false if string is a name.

{:id="StringFunctions.isStartsWith" class="method returns boolean"} <i/> isStartsWith(*string*{:.text},*character*{:.text},*ignoreCapitalization*{:.boolean})
: Return true or false if string start with specific character.

{:id="StringFunctions.isValidEmailAddress" class="method returns boolean"} <i/> isValidEmailAddress(*string*{:.text})
: Return true or false if string is a valid email address.

## Task  {#Task}

Component for Task



### Properties  {#Task-Properties}

{:.properties}
None


### Events  {#Task-Events}

{:.events}

{:id="Task.Reached"} Reached()
: Occurs when the delay and interval are reached.

### Methods  {#Task-Methods}

{:.methods}

{:id="Task.CreateSchedule" class="method"} <i/> CreateSchedule(*delay*{:.number},*interval*{:.number})
: Create and start schedule.

{:id="Task.StopSchedule" class="method"} <i/> StopSchedule()
: Stop schedule.

## TestBackground  {#TestBackground}

Component for TestBackground



### Properties  {#TestBackground-Properties}

{:.properties}
None


### Events  {#TestBackground-Events}

{:.events}
None


### Methods  {#TestBackground-Methods}

{:.methods}

{:id="TestBackground.Start" class="method"} <i/> Start()
: To start testing

## TextBoxUtils  {#TextBoxUtils}

Component for TextBoxUtils



### Properties  {#TextBoxUtils-Properties}

{:.properties}
None


### Events  {#TextBoxUtils-Events}

{:.events}
None


### Methods  {#TextBoxUtils-Methods}

{:.methods}

{:id="TextBoxUtils.ChangeVisibility" class="method"} <i/> ChangeVisibility()
: To change visibility of textbox.

{:id="TextBoxUtils.GotFocus" class="method"} <i/> GotFocus(*textBoxNum*{:.number})
: To occurs GotFocus event.

{:id="TextBoxUtils.Initialize" class="method"} <i/> Initialize(*label*{:.component},*stateOn*{:.number},*stateOff*{:.number})
: To initialize components.

{:id="TextBoxUtils.InitializeOTP" class="method"} <i/> InitializeOTP(*labelError*{:.component},*textBox1*{:.component},*textBox2*{:.component},*textBox3*{:.component},*textBox4*{:.component},*textBox5*{:.component},*textBox6*{:.component})
: To initialize components for otp code with 6 textboxs.

{:id="TextBoxUtils.InputType" class="method returns number"} <i/> InputType()
: To get input type of textbox.

{:id="TextBoxUtils.OnTextChanged" class="method"} <i/> OnTextChanged(*textBoxNum*{:.number})
: To occurs OnTextChanged event.

{:id="TextBoxUtils.Reset" class="method"} <i/> Reset()
: To reset textbox.

{:id="TextBoxUtils.ShowError" class="method"} <i/> ShowError()
: To show error label.

{:id="TextBoxUtils.Text" class="method returns text"} <i/> Text()
: Get text of all textbox.

{:id="TextBoxUtils.isEmpty" class="method returns boolean"} <i/> isEmpty()
: Return true or false if textbox is empty or less then 6 digits.

## Utils  {#Utils}

Component for Utils



### Properties  {#Utils-Properties}

{:.properties}
None


### Events  {#Utils-Events}

{:.events}
None


### Methods  {#Utils-Methods}

{:.methods}

{:id="Utils.CheckboxProperty" class="method returns text"} <i/> CheckboxProperty(*select*{:.text})
: Return a property name.

{:id="Utils.Component" class="method returns text"} <i/> Component(*select*{:.text})
: Return a component.

{:id="Utils.HorizontalArrangementProperty" class="method returns text"} <i/> HorizontalArrangementProperty(*select*{:.text})
: Return a property name.

{:id="Utils.LabelProperty" class="method returns text"} <i/> LabelProperty(*select*{:.text})
: Return a property name.

{:id="Utils.SpaceProperty" class="method returns text"} <i/> SpaceProperty(*select*{:.text})
: Return a property name.

{:id="Utils.VerticalArrangementProperty" class="method returns text"} <i/> VerticalArrangementProperty(*select*{:.text})
: Return a property name.
