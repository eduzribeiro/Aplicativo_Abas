/*
 * Copyright (c) 2015. Fernando Pujaico Rivera <fernando.pujaico.rivera@gmail.com>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package net.trucomanx.pdsplibj.pdsdf;

import  net.trucomanx.pdsplibj.pdsra.PdsVector;

/**
 * Esta classe implementa um filtro FIR NLMS (Normalized - Least Mean Square) de ordem M.<br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsFirNlms/filtrobloco.png" alt="Filtro NLMS FIR"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsFirNlms/lms.png" alt="Filtro NLMS FIR"></center><br>
 * <br>
 * <center><img src="{@docRoot}/doc/imagenes/PdsFirNlms/firlms.png" alt="Actualização de pesos"></center><br>
 * <br>
 * <br>
 * <b>FIR_LMS:</b>
 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
 * $\\mathbf{X}[n]=(X[n],X[n-1],...,X[n-M])^T$
 * }</center>
 * <br>
 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
 * $\\mathbf{H}[n]=(H_0[n],H_1[n],...,X_M[n])^T$
 * }</center>
 * <br>
 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
 * $Y[n] = {\\mathbf{H}[n]}^T \\mathbf{X}[n] = \\sum \\limits^{M}_{i=0} H_i[n] X[n-i]$
 * }</center>
 * <br>
 * <br> <b>SAIDA:</b>
 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
 * $E[n]=D[n]-Y[n]$
 * }</center>
 * <br>
 * <br> <b>Reconfigura:</b>
 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
 * $\\mathbf{H}[n+1]=\\mathbf{H}[n]+ \\frac{ \\mu E[n]\\mathbf{X}[n]}{{\\mathbf{X}[n]}^T\\mathbf{X}[n] + \\lambda}$
 * }</center>
 *
 * <br> <b>Valor ótimo de mu:</b>
 * <center>{@latex.ilb %preamble{\\usepackage{amssymb}} %resolution{150} 
 * $\\mu_{opt}=\\frac{Mean[|E-V|^2]}{Mean[|E|^2]}$
 * }</center>
 * Isto indica que {@latex.inline $\\mu \\leq 1$} ({@latex.inline $E$} é uma aproximação de {@latex.inline $V$}).
 * <br>
 *
 * <br><br> Para usar esta classe é necessário escrever:
 *  <pre>  import net.trucomanx.pdsplibj.pdsdf.PdsFirNlms; </pre>
 *
 * <br>Para usar este filtro pode-se usar este código de exemplo:
 *  <pre>  
 *  int M=12;                      // Ordem do filtro.
 *  double Mu=0.6;                // Fator de aprendizagem
 *  PdsVector Dat;
 *  double Y,E;
 *
 *  PdsFirNlms Filtro = new PdsFirNlms(Mu,M);
 *  
 *  Filtro.SetLambda(0.0000001);   // Qualquer valor lambda muito pequeno.
 *
 *  Dat=Filtro.EvaluateValue(Dvalue,Xvalue);
 *  Y=Dat.GetValue(0);
 *  E=Dat.GetValue(1);
 *  </pre>
 * 
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.05
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java </a>
 * @see PdsFir
 */
public class PdsFirNlms {
    private int Work;
    private double Mu;
    private double Lambda;
    private PdsFir Fir;
    private PdsVector Data;


    /**
     * Este constructor crea un filtro FIR NLMS.
     *
     * Os parâmetros {@latex.inline $H_i[n]$} do filtro FIR tem um
     * valor inicial de {@latex.inline $H_i[n]=1/(1+M)$}. Por defeito o filtro FIR NLMS estará
     *  auto configurando-se continuamente, a no ser que se desabilite com
     *  o método {@link #Disable()}.
     *
     * @param mu Este é passo da constante de adaptação.
     * @param M É o ordem do filtro: {@latex.inline $Y[n]=X[n] H_0[n] + X[n-1] H_1[n] + ... + X[n-M] H_M[n]$}
     **/
    public PdsFirNlms(double mu,int M) {

        this.Data   = new PdsVector(2);

        this.Fir    = new PdsFir(M);
        for(int i=0;i<=M;i++)   this.Fir.SetHValue(1.0/(M+1),i);

        this.Work=1;
        this.Mu=mu;
        this.Lambda=0;
    }

    /**
     * Desabilita a reconfiguração dos pesos {@latex.inline $H_i[n]$} do filtro FIR NLMS.
     */
    public void Disable(){
        this.Work=0;
    }

    /**
     * Habilita la reconfiguração de los pesos {@latex.inline $H_i[n]$} del filtro FIR NLMS.
     */
    public void Enable(){
        this.Work=1;
    }

    /**
     * Coloca o valor Mu do filtro FIR NLMS.
     *
     * @param mu É o passo da constante de adaptação.
     */
    public void SetMu(double mu){
        this.Mu=mu;
    }

    /**
     * Coloca o valor de lambda{@latex.inline $\\equiv \\lambda$} do filtro FIR NLMS.
     *
     * 
     * @param lambda É um parâmetro que tenta prever que de um erro por divisão por zero se 
     *               a entrada X é toda zero. Aconselha-se que lambda seja muito menor que  1.0.
     */
    public void SetLambda(double lambda){
        this.Lambda=lambda;
    }

    /**
     * Este método avalia o filtro FIR LMS normalizado
     *
     * O filtro é avaliado de jeito que no filtro entra X e D e é
     * retirado um valor filtrado Y e a diferença E.
     *
     * @param D É um valor na entrada D.
     * @param X É um valor na entrada X.
     * @return  Retorna um vector com os valores de {Y, E} depois de avaliar o filtro FIR NLMS.
     */
    public PdsVector EvaluateValue(double D,double X){
        double Y,E,XtX;
        int i,N;
        double Hi;

        Y=this.Fir.EvaluateValue(X);
        E=D-Y;

        if(this.Work==1) {
            N=this.Fir.GetDimension();

            XtX = 0;
            for (i = 0; i <= N; i++) {
                XtX = XtX + this.Fir.GetXValue(i)*this.Fir.GetXValue(i);
            }

            for(i=0;i<=N;i++) {
                Hi=this.Fir.GetHValue(i);

                Hi = Hi + this.Mu*E*this.Fir.GetXValue(i) /(this.Lambda + XtX);

                this.Fir.SetHValue(Hi,i);
            }

        }

        Data.SetValue(0,Y);
        Data.SetValue(1,E);
        return this.Data;
    }
}
