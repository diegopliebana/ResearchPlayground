function E=RMHC(C,n,sigma,r)
E=[];
X=zeros(n);
for test=1:100
    evals=0;
    while(evals<10000000)
        idx=randi(n);
        Y=X;
        Y(idx)=1-Y(idx);
        if( NoisyTrap(C,Y,sigma) >= NoisyTrap(C,X,sigma) )
            X=Y;
        end
        evals=evals + 2*r;
    end
    E=[E evals];
end
end