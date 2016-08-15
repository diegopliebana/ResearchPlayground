function res=RMHCNoisyTrap(C,n,sigma,r)
res=[];
X=randi(2,1,n)-1;
evals=0;
fx=NaN;
rhist=0;
while(evals<1000000000)
    idx=randi(n);
    Y=X;
    Y(idx)=1-Y(idx);
    fx=NoisyTrap(C,X,sigma,r);
    %fx=(fx*rhist + NoisyTrap(C,X,sigma,r)*r)/(rhist+r);
    fy=NoisyTrap(C,Y,sigma,r);
    if( fy >= fx )
        X=Y;
        fx=fy;
        rhist=r;
    else
        rhist=rhist+r;
    end
    evals=evals + 2*r;
    if(sum(X)==0)
        break
    end
end
res=[fx evals];
end