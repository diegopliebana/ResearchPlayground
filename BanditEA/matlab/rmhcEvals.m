function E=rmhcEvals(n,r)
pc=1/2+erf(sqrt(r)/2)/2;
%p=erf(sqrt(r)/2);
%p=erf(1/r);
disp(sprintf('dimension=%d, resamples=%d, p=%f',n,r,pc));
E=[];
for test=1:100
    evals=0;
    i=randi(n+1,1)-1;
    while(i<n && evals<1000000)
        p=rand;
        if(p>=i/n)
            if(rand<=pc)
                i=i+1;
            end
        else
            if(rand>pc)
                i=i-1;
            end
        end
        evals=evals + 2*r;
    end
    E=[E evals];
end
end