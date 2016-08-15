P=[0.1:0.1:0.9];
E=[];

for p=[0.1:0.1:0.9]
evals=1/(p^2) + 1/p + 1/p;
E=[E evals];
end
plot(P,E)

p=1/2+erf(1/2)/2
evals=1/(p^2) + 1/p + 1/p
evals*2