package %1.service
{
	////////////////////////////////////////////////////////////
	// Imports
	import com.arahant.core.control.validating.*;
	import flash.utils.Dictionary;


	////////////////////////////////////////////////////////////
	// Class Definition
	public class ServiceValidation extends Validation
	{
		////////////////////////////////////////////////////////////
		// Static Vars
		private static var c_vmap:Dictionary = ServiceValidation.buildData();

		
		////////////////////////////////////////////////////////////
		// Internal Helpers
		override protected function get data():Dictionary
		{
		  	return c_vmap;
		}
		
		private static function buildData():Dictionary
 		{
 			var vmap:Dictionary = new Dictionary();