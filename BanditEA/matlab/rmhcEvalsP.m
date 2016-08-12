function E=rmhcEvalsP(n,r)
pc=1/2+erf(sqrt(r)/2)/2;
disp(sprintf('dimension=%d, resamples=%d, p=%f',n,r,pc));
E=[];
for test=1:100
    evals=0;
    i=randi(n+1,1)-1;
    while(i<n && evals<1000000)
        p=rand;
        if(p>=i/n)
            p1=randn/sqrt(r);
            p2=randn/sqrt(r);
            if(p2+1>=p1)
                i=i+1;
            end
        else
            p1=randn/sqrt(r);
            p2=randn/sqrt(r);
            if(p2>=p1+1)
                i=i-1;
            end
        end
        evals=evals+r*2;
    end
    evals
    E=[E evals];
end
end