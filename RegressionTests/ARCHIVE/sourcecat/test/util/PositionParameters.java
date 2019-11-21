package util;

public class PositionParameters {
	public static final double FLOAT_TOLERANCE = 0.0000000001;

	public static class PositionalSearchParameters {
		boolean performRaSearch = false;
		boolean performDecSearch = false;
		public double raMin1 = 0;
		public double raMax1 = -FLOAT_TOLERANCE;
		public double raMin2 = 0;
		public double raMax2 = -FLOAT_TOLERANCE;
		public double decMin = 0;
		public double decMax = -FLOAT_TOLERANCE;
	};
	
	public static PositionalSearchParameters calculatePositionalSearchParameters(final Double ra, final Double dec, final Double radius){
		PositionalSearchParameters parameters = new PositionalSearchParameters();
		if (dec != null && dec > -90 - FLOAT_TOLERANCE
				&& dec < 90 + FLOAT_TOLERANCE && ra != null
				&& ra > -FLOAT_TOLERANCE && ra <= 360 && radius != null
				&& radius > -FLOAT_TOLERANCE) {
			if (Math.abs(dec) + radius < 90) {
				// degrees(2*asin(sin(radians(sr)/2)/cos(radians(target_decl))))
				final double radelta = Math.toDegrees(2 * Math.asin(Math
						.sin(Math.toRadians(radius) / 2.0)
						/ Math.cos(Math.toRadians(dec))));
				parameters.performRaSearch = true;
				final double raMin = ra - radelta;
				final double raMax = ra + radelta + FLOAT_TOLERANCE;
				if (raMin < 0) {
					parameters.raMin1 = 360 + raMin;
					parameters.raMax1 = 360 + FLOAT_TOLERANCE;
					parameters.raMin2 = 0;
					parameters.raMax2 = raMax;
				} else if (raMax > 360) {
					parameters.raMin1 = raMin;
					parameters.raMax1 = 360 + FLOAT_TOLERANCE;
					parameters.raMin2 = 0;
					parameters.raMax2 = raMax - 360.0;
				} else {
					parameters.raMin1 = raMin;
					parameters.raMax1 = raMax;
				}
			}
			parameters.performDecSearch = true;
			final double decdelta = 2.0 * radius;
			parameters.decMin = dec - decdelta;
			parameters.decMax = dec + decdelta + FLOAT_TOLERANCE;
		}

		return parameters;

	}

	public double hhmmss2decimalsRA(String hhmmss) throws Exception {
		return hhmmss2decimals(hhmmss, 15, false);
	} 
	 
	public double hhmmss2decimalsDEC(String hhmmss) throws Exception {
		return hhmmss2decimals(hhmmss, 1, true);
	} 
	 
	private static double hhmmss2decimals(String hhmmss, int i, Boolean acceptsNegativeValues) throws Exception {
	  String[] hhmmssParts = hhmmss.split(":");
	  String hours = hhmmssParts.length > 0 ? hhmmssParts[0] : "0.0";
	  String minutes = hhmmssParts.length > 1 ? hhmmssParts[1] : "0.0";
	  String seconds = hhmmssParts.length > 2 ? hhmmssParts[2] : "0.0";

	  Boolean isNegative = Float.parseFloat(hours) < 0;
	  if (isNegative) {
		  // remove the minus sign
	      hours = hours.substring(1);	
	  }

	  if (hhmmssParts.length < 3 
		|| hours.length() == 0 
		|| minutes.length() == 0
	    || seconds.length() == 0
		|| (isNegative && !acceptsNegativeValues)) {
		throw new Exception("Invalid input: " + hhmmss);
	  }
	  
	  return (isNegative ? -1 : 1) 
				* i 
				* (Float.parseFloat(hours) + Float.parseFloat(minutes) / 60.0 + Float.parseFloat(seconds) / 3600.0);
	}

}
