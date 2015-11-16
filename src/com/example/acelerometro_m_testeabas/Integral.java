package com.example.acelerometro_m_testeabas;

public class Integral {
	private double last_g;
	private double last_t;
	private double Tau;
	
	public Integral()
	{
		this.last_g=0;
		this.last_t=0;
		this.Tau=0;
	}

	public Integral(double tau)
	{
		this.last_g=0;
		this.last_t=0;
		this.Tau=tau;
	}

	public double EvaluateValue(double f,double t)
	
	{
		double g;
		
		if (last_t==0)	last_t=t;

		g=f*(t-last_t)+last_g;

		if((f==0)&&(this.Tau!=0))
		{
			g=g*Math.exp(-(t-last_t)/this.Tau);
		}

		last_g=g;
		last_t=t;
		
		return g;
	}
}
