import LoginForm from "@/components/ui/loginform";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"

function Register() {
    function handleRegister(values: { username: string; password: string}) {
        console.log(values)
    }

    return(<div className="h-screen flex justify-center items-center align-middle">
        <Card className="w-[350]px">
            <CardHeader>
                <CardTitle>Register</CardTitle>
            </CardHeader>
            <CardContent>
                <LoginForm onSubmit={handleRegister} />
            </CardContent>
        </Card>
        </div>
    )
}

export default Register;