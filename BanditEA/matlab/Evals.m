function evals=rmhcEvals(n,r)
p=1/2+erf(sqrt(r)/2)/2;
evals=1;
N=1;
for i=1:n-1
    %A=p*(1-p)*n*n + p*i*(p+2*i)*n + (2-2*p-i-2*p*i)*p*i;
    %B=(1-p+p*p)*n*n + p*i*(1-2*p-i)*n + (2*p*i+p-1)*p*i;
    
    %A=2*p*(n-p-1)*i*i + p*(2-p+n*p)*i + p*(1-p)*n*n;
    %B=p*(2*p-n)*i*i + p*(n-2*n*p+p-1)*i + (1-p+p*p)*n*n;
    
    %A=n*n+(3*p*i-2*p*p*i-i)*n+(2*p*i-2*p*p*i);
    %B=-(p*i*i+p*p*i*i)*n*n + (i+p-p*p*i-3*p*i)*n;
    
    %A=(n-i+1)*p/n%/(i+n*p-2*p*i);
    %B=(i+1)*(1-p)/n%/(i+n*p-2*p*i);
    
    %h=((A/B)^i-(A/B)^n)/(1-(A/B)^n)
    %evals=evals+1/h;
    %A=i*(1-p)/(i+(n-2*i)*p);
    %B=-i/(i+(n-2*i)*p);
    %evals=evals+A*N+B;
    %N=A*N+B;
    evals = evals + n/((n-i)*p);
    evals
end
evals=evals*r*2;
end