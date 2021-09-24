function Errors({errors=[]}) {

    return (
        <>
            {errors.length > 0 && (
                <div className="alert alert-danger">
                    <ul>
                        {errors.map(error => (
                            <li key={error}>{error}</li>
                        ))}
                    </ul>
                </div>)}
        </>
    );
}

export default Errors;