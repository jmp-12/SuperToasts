package com.extlibsupertoasts;

import android.content.Context;
import android.widget.Toast;

public class SuperImageToast extends Toast
{
	
	private static final String ERROR_CONTEXTNULL= "The Context that you passed was not an instance of the Context class! (SuperToast)";
	private static final String ERROR_CONTEXTINSTANCE = "The Context that you passed was null! (SuperToast)";
	
	private Context context;
	
	
	public SuperImageToast(final Context context) 
	{
		
		super(context);
		
			if(context != null)
			{
				
				if(context instanceof Context)
				{
					
					this.context = context;
					
				}
				
				else
				{
					
					throw new IllegalArgumentException(ERROR_CONTEXTINSTANCE);
					
				}
				
			}
			
			else
			{
				
				throw new IllegalArgumentException(ERROR_CONTEXTNULL);
				
			}
		
	}
	
	
	
	
	

}
